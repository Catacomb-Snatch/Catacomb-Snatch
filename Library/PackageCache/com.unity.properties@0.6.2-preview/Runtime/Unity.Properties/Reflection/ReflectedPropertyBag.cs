using System.Collections.Generic;

namespace Unity.Properties.Reflection
{
    public class ReflectedPropertyBag<TContainer> : PropertyBag<TContainer>
    {
        interface IPropertyProxy
        {
            void Accept<TVisitor>(ref TContainer container, TVisitor visitor, ref ChangeTracker changeTracker) where TVisitor : IPropertyVisitor;
            void FindProperty<TCallback>(ref TContainer container, ref ChangeTracker changeTracker, ref TCallback callback) where TCallback : IPropertyGetter<TContainer>;
        }

        struct PropertyProxy<TProperty, TValue> : IPropertyProxy
            where TProperty : IProperty<TContainer, TValue>
        {
            public TProperty Property;

            public void Accept<TVisitor>(ref TContainer container, TVisitor visitor, ref ChangeTracker changeTracker) where TVisitor : IPropertyVisitor =>
                visitor.VisitProperty<TProperty, TContainer, TValue>(Property, ref container, ref changeTracker);

            public void FindProperty<TCallback>(ref TContainer container, ref ChangeTracker changeTracker, ref TCallback callback) where TCallback : IPropertyGetter<TContainer>
                => callback.VisitProperty<TProperty, TValue>(Property, ref container, ref changeTracker);
        }

        struct CollectionPropertyProxy<TProperty, TValue> : IPropertyProxy
            where TProperty : ICollectionProperty<TContainer, TValue>
        {
            public TProperty Property;

            public void Accept<TVisitor>(ref TContainer container, TVisitor visitor, ref ChangeTracker changeTracker) where TVisitor : IPropertyVisitor =>
                visitor.VisitCollectionProperty<TProperty, TContainer, TValue>(Property, ref container, ref changeTracker);

            public void FindProperty<TCallback>(ref TContainer container, ref ChangeTracker changeTracker, ref TCallback callback) where TCallback : IPropertyGetter<TContainer>
                => callback.VisitCollectionProperty<TProperty, TValue>(Property, ref container, ref changeTracker);
        }

        readonly List<IPropertyProxy> m_PropertiesList = new List<IPropertyProxy>();
        readonly Dictionary<string, IPropertyProxy> m_PropertiesDictionary = new Dictionary<string, IPropertyProxy>();

        public void AddProperty<TProperty, TValue>(TProperty property) where TProperty : IProperty<TContainer, TValue>
        {
            var proxy = new PropertyProxy<TProperty, TValue> {Property = property};
            m_PropertiesDictionary.Add(property.GetName(), proxy);
            m_PropertiesList.Add(proxy);
        }

        public void AddCollectionProperty<TProperty, TValue>(TProperty property) where TProperty : ICollectionProperty<TContainer, TValue>
        {
            var proxy = new CollectionPropertyProxy<TProperty, TValue> {Property = property};
            m_PropertiesDictionary.Add(property.GetName(), proxy);
            m_PropertiesList.Add(proxy);
        }

        public override void Accept<TVisitor>(ref TContainer container, TVisitor visitor, ref ChangeTracker changeTracker)
        {
            for (var i = 0; i < m_PropertiesList.Count; i++)
            {
                m_PropertiesList[i].Accept(ref container, visitor, ref changeTracker);
            }
        }

        public override bool FindProperty<TCallback>(string name, ref TContainer container, ref ChangeTracker changeTracker, ref TCallback action)
        {
            if (m_PropertiesDictionary.TryGetValue(name, out var proxy))
            {
                proxy.FindProperty(ref container, ref changeTracker, ref action);
                return true;
            }

            return false;
        }
    }
}
