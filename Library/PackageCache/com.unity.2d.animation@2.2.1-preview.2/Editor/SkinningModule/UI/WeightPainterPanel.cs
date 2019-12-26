using System;
using System.Collections.Generic;
using UnityEditor.UIElements;
using UnityEngine;
using UnityEngine.UIElements;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class WeightPainterPanel : VisualElement
    {
        public class WeightPainterPanelFactory : UxmlFactory<WeightPainterPanel, WeightPainterPanelUxmlTraits> {}
        public class WeightPainterPanelUxmlTraits : VisualElement.UxmlTraits {}
        public static readonly string kNone = "None";

        private WeightPainterMode m_PaintMode;
        private EnumField m_ModeField;
        private Toggle m_NormalizeToggle;
        private IntegerField m_HardnessField;
        private IntegerField m_StepField;
        private IntegerField m_SizeField;
        private FloatField m_AmountField;
        private Slider m_AmountSlider;
        private VisualElement m_BonePopupContainer;
        private PopupField<string> m_BonePopup;
        private bool m_SliderActive = false;
        private WeightInspectorIMGUIPanel m_WeightInspectorPanel;
        private UnityEngine.UIElements.PopupWindow m_PopupWindow;

        public event Action<int> bonePopupChanged = (s) => {};
        public event Action sliderStarted = () => {};
        public event Action<float> sliderChanged = (s) => {};
        public event Action sliderEnded = () => {};
        public event Action weightsChanged = () => {};

        public WeightPainterMode paintMode
        {
            get { return m_PaintMode; }
            set
            {
                if (value == m_PaintMode)
                    return;

                m_PaintMode = value;
                if (m_PaintMode == WeightPainterMode.Brush)
                {
                    RemoveFromClassList("SliderMode");
                    AddToClassList("BrushMode");
                }
                else
                {
                    RemoveFromClassList("BrushMode");
                    AddToClassList("SliderMode");
                }
            }
        }

        public string title
        {
            set { m_PopupWindow.text = value; }
        }

        public WeightEditorMode mode
        {
            get { return (WeightEditorMode)m_ModeField.value; }
            set { m_ModeField.value = value; }
        }

        public int boneIndex
        {
            get { return m_BonePopup.index - 1; }
        }

        public int size
        {
            get { return m_SizeField.value; }
            set { m_SizeField.value = value; }
        }

        public int hardness
        {
            get { return m_HardnessField.value; }
            set { m_HardnessField.value = value; }
        }

        public int step
        {
            get { return m_StepField.value; }
            set { m_StepField.value = value; }
        }

        public bool normalize
        {
            get { return m_NormalizeToggle.value; }
            set { m_NormalizeToggle.value = value; }
        }

        public float amount
        {
            get { return m_AmountField.value; }
            set { m_AmountField.value = value; }
        }

        public WeightPainterPanel()
        {
            styleSheets.Add(Resources.Load<StyleSheet>("WeightPainterPanelStyle"));
            if (EditorGUIUtility.isProSkin)
                AddToClassList("Dark");

            paintMode = WeightPainterMode.Brush;
            AddToClassList("BrushMode");

            RegisterCallback<MouseDownEvent>((e) => { e.StopPropagation(); });
            RegisterCallback<MouseUpEvent>((e) => { e.StopPropagation(); });
        }

        public void BindElements()
        {
            m_ModeField = this.Q<EnumField>("ModeField");
            m_BonePopupContainer = this.Q<VisualElement>("BoneEnumPopup");
            m_NormalizeToggle = this.Q<Toggle>("NormalizeToggle");
            m_SizeField = this.Q<IntegerField>("SizeField");
            m_HardnessField = this.Q<IntegerField>("HardnessField");
            m_StepField = this.Q<IntegerField>("StepField");
            m_AmountSlider = this.Q<Slider>("AmountSlider");
            m_AmountField = this.Q<FloatField>("AmountField");
            m_AmountField.isDelayed = true;
            m_WeightInspectorPanel = this.Q<WeightInspectorIMGUIPanel>("WeightsInspector");
            m_PopupWindow = this.Q<UnityEngine.UIElements.PopupWindow>();

            LinkSliderToIntegerField(this.Q<Slider>("HardnessSlider"), m_HardnessField);
            LinkSliderToIntegerField(this.Q<Slider>("StepSlider"), m_StepField);

            m_ModeField.RegisterValueChangedCallback((evt) =>
                {
                    SetupMode();
                });

            m_AmountSlider.RegisterValueChangedCallback((evt) =>
                {
                    if (!evt.Equals(m_AmountField.value))
                        m_AmountField.value = (float)System.Math.Round((double)evt.newValue, 2);
                });
            m_AmountField.RegisterValueChangedCallback((evt) =>
                {
                    var newValue = Mathf.Clamp(evt.newValue, m_AmountSlider.lowValue, m_AmountSlider.highValue);

                    if (focusController.focusedElement == m_AmountField && !newValue.Equals(m_AmountSlider.value))
                    {
                        sliderStarted();
                        sliderChanged(newValue);
                        sliderEnded();
                        Focus();
                        m_AmountField.value = 0f;
                        m_AmountSlider.value = 0f;
                    }
                });

            m_WeightInspectorPanel.weightsChanged += () => weightsChanged();
        }

        public void SetActive(bool active)
        {
            this.Q("Amount").SetEnabled(active);
        }

        private void SetupMode()
        {
            var boneElement = this.Q<VisualElement>("Bone");
            boneElement.SetHiddenFromLayout(mode == WeightEditorMode.Smooth);
            SetupAmountSlider();
        }

        private void SetupAmountSlider()
        {
            if (paintMode == WeightPainterMode.Slider)
            {
                if (mode == WeightEditorMode.Smooth)
                {
                    m_AmountSlider.lowValue = 0.0f;
                    m_AmountSlider.highValue = 8.0f;
                }
                else
                {
                    m_AmountSlider.lowValue = -1.0f;
                    m_AmountSlider.highValue = 1.0f;
                }
            }
        }

        private void LinkSliderToIntegerField(Slider slider, IntegerField field)
        {
            slider.RegisterValueChangedCallback((evt) =>
                {
                    if (!evt.newValue.Equals(field.value))
                        field.value = Mathf.RoundToInt(evt.newValue);
                });
            field.RegisterValueChangedCallback((evt) =>
                {
                    if (!evt.newValue.Equals((int)slider.value))
                        slider.value = evt.newValue;
                });
        }

        public void UpdateWeightInspector(ISpriteMeshData spriteMeshData, string[] boneNames, ISelection<int> selection, ICacheUndo cacheUndo)
        {
            m_WeightInspectorPanel.weightInspector.spriteMeshData = spriteMeshData;
            m_WeightInspectorPanel.weightInspector.boneNames = ModuleUtility.ToGUIContentArray(boneNames);
            m_WeightInspectorPanel.weightInspector.selection = selection;
            m_WeightInspectorPanel.weightInspector.cacheUndo = cacheUndo;
        }

        public void UpdatePanel(string[] boneNames)
        {
            SetupMode();
            UpdateBonePopup(boneNames);
        }

        private void UpdateBonePopup(string[] names)
        {
            if (m_BonePopup != null)
                m_BonePopup.RemoveFromHierarchy();

            m_BonePopup = new PopupField<string>(new List<string>(names), 0);
            m_BonePopup.name = "BonePopupField";
            m_BonePopup.RegisterValueChangedCallback((evt) =>
                {
                    bonePopupChanged(boneIndex);
                });
            m_BonePopupContainer.Add(m_BonePopup);
        }

        internal void SetBoneSelectionByName(string boneName)
        {
            if (m_BonePopup != null)
                m_BonePopup.value = boneName;
        }

        public static WeightPainterPanel GenerateFromUXML()
        {
            var visualTree = Resources.Load("WeightPainterPanel") as VisualTreeAsset;
            var clone = visualTree.CloneTree().Q<WeightPainterPanel>("WeightPainterPanel");

            // EnumField can only get type of Enum from the current running assembly when defined through UXML
            // Manually create the EnumField here
            var mode = clone.Q<VisualElement>("Mode");
            var modeField = new EnumField(WeightEditorMode.AddAndSubtract);
            modeField.name = "ModeField";
            mode.Add(modeField);

            clone.BindElements();
            return clone;
        }

        public override void HandleEvent(EventBase evt)
        {
            base.HandleEvent(evt);
            DoDeltaSliderBehaviour(m_AmountSlider, evt);
        }

        private void DoDeltaSliderBehaviour(Slider slider, EventBase evt)
        {
            if (evt.target == slider)
            {
                if (evt is MouseCaptureEvent)
                {
                    m_SliderActive = true;
                    if (sliderStarted != null)
                        sliderStarted();
                }

                if (m_SliderActive && evt is ChangeEvent<float>)
                {
                    if (sliderChanged != null)
                        sliderChanged(slider.value);
                }

                if (m_SliderActive && evt is MouseCaptureOutEvent)
                {
                    m_SliderActive = false;
                    if (sliderEnded != null)
                        sliderEnded();
                    slider.value = 0f;
                }
            }
        }
    }
}
