using UnityEngine;
using UnityEditor;
using UnityEngine.Experimental.U2D.Animation;

namespace UnityEditor.Experimental.U2D.Animation
{

#if ENABLE_ENTITIES

    [CustomEditor(typeof(SpriteSkinEntity))]
    [CanEditMultipleObjects]
    class SpriteSkinEntityEditor : Editor
    {
        public override void OnInspectorGUI()
        {
          
        }
    }
#endif

}
