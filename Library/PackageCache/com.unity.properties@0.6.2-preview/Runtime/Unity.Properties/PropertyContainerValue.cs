using System;

namespace Unity.Properties
{
    public static partial class PropertyContainer
    {
        const int k_ResultSuccess = 0;
        const int k_ResultErrorConvert = -1;
        const int k_ResultErrorReadOnly = -2;

        struct GetValueAction<TContainer, TDestinationValue> : IPropertyGetter<TContainer>
        {
            public TDestinationValue DstValue;
            public int Result;

            public void VisitProperty<TProperty, TSourceValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker)
                where TProperty : IProperty<TContainer, TSourceValue>
            {
                if (!TypeConversion.TryConvert(property.GetValue(ref container), out DstValue))
                {
                    Result = k_ResultErrorConvert;
                }
            }

            public void VisitCollectionProperty<TProperty, TSourceValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker)
                where TProperty : ICollectionProperty<TContainer, TSourceValue>
            {
                VisitProperty<TProperty, TSourceValue>(property, ref container, ref changeTracker);
            }
        }

        struct SetValueAction<TContainer, TSourceValue> : IPropertyGetter<TContainer>
        {
            public TSourceValue SrcValue;
            public int Result;

            public void VisitProperty<TProperty, TDestinationValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker)
                where TProperty : IProperty<TContainer, TDestinationValue>
            {
                if (property.IsReadOnly)
                {
                    Result = k_ResultErrorReadOnly;
                    return;
                }
                
                if (!TypeConversion.TryConvert<TSourceValue, TDestinationValue>(SrcValue, out var dstValue))
                {
                    Result = k_ResultErrorConvert;
                    return;
                }

                if (CustomEquality.Equals(dstValue, property.GetValue(ref container)))
                {
                    return;
                }

                property.SetValue(ref container, dstValue);
                changeTracker.IncrementVersion<TProperty, TContainer, TDestinationValue>(property, ref container);
            }

            public void VisitCollectionProperty<TProperty, TDestinationValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker)
                where TProperty : ICollectionProperty<TContainer, TDestinationValue>
            {
                VisitProperty<TProperty, TDestinationValue>(property, ref container, ref changeTracker);
            }
        }

        struct GetCountAction<TContainer> : IPropertyGetter<TContainer>
        {
            public int Count;
            
            public void VisitProperty<TProperty, TValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker) where TProperty : IProperty<TContainer, TValue>
            {
                throw new NotImplementedException($"Failed to {nameof(GetCount)}. Property is not a collection");
            }

            public void VisitCollectionProperty<TProperty, TValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker) where TProperty : ICollectionProperty<TContainer, TValue>
            {
                Count = property.GetCount(ref container);
            }
        }

        struct SetCountAction<TContainer> : IPropertyGetter<TContainer>
        {
            readonly int m_Count;

            public SetCountAction(int count) => m_Count = count;

            public void VisitProperty<TProperty, TValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker) where TProperty : IProperty<TContainer, TValue>
            {
                throw new NotImplementedException($"Failed to {nameof(SetCount)}. Property is not a collection");
            }

            public void VisitCollectionProperty<TProperty, TValue>(TProperty property, ref TContainer container, ref ChangeTracker changeTracker) where TProperty : ICollectionProperty<TContainer, TValue>
            {
                property.SetCount(ref container, m_Count);
            }
        }

        /// <summary>
        /// Sets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container whose data will be set.</param>
        /// <param name="name">The property name to set.</param>
        /// <param name="value">The value to assign to the property.</param>
        /// <param name="versionStorage">The version storage to increment if the value is changed.</param>
        public static void SetValue<TContainer, TValue>(ref TContainer container, string name, TValue value, IVersionStorage versionStorage = null)
        {
            var changeTracker = new ChangeTracker(versionStorage);
            SetValue(ref container, name, value, ref changeTracker);
        }

        /// <summary>
        /// Sets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container whose data will be set.</param>
        /// <param name="name">The property name to set.</param>
        /// <param name="value">The value to assign to the property.</param>
        /// <param name="changeTracker">The change tracker to increment if the value changes.</param>
        public static void SetValue<TContainer, TValue>(ref TContainer container, string name, TValue value, ref ChangeTracker changeTracker)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                throw new InvalidOperationException($"Failed to resolve property bag for ContainerType=[{typeof(TContainer)}]");
            }

            var action = new SetValueAction<TContainer, TValue> {SrcValue = value};

            if (!propertyBag.FindProperty(name, ref container, ref changeTracker, ref action))
            {
                throw new InvalidOperationException($"Failed to find property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }

            if (action.Result == k_ResultErrorConvert)
            {
                throw new InvalidOperationException($"Failed assign ValueType=[{typeof(TValue)}] to property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }
            
            if (action.Result == k_ResultErrorReadOnly)
            {
                throw new InvalidOperationException("Property is ReadOnly");
            }
        }

        /// <summary>
        /// Gets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name to get.</param>
        /// <returns>The value of the property converted to the given type.</returns>
        public static TValue GetValue<TContainer, TValue>(ref TContainer container, string name)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                throw new InvalidOperationException($"Failed to resolve property bag for ContainerType=[{typeof(TContainer)}]");
            }

            var changeTracker = new ChangeTracker();
            var action = new GetValueAction<TContainer, TValue>();

            if (!PropertyBagResolver.Resolve<TContainer>().FindProperty(name, ref container, ref changeTracker, ref action))
            {
                throw new InvalidOperationException($"Failed to find property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }

            if (action.Result == k_ResultErrorConvert)
            {
                throw new InvalidOperationException($"Failed get ValueType=[{typeof(TValue)}] from property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }

            return action.DstValue;
        }


        /// <summary>
        /// Gets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name to get.</param>
        /// <param name="value">Contains the value if the property is found and the type can be converted; otherwise this is set to default.</param>
        /// <returns>True if the property was found and the value was converted.</returns>
        public static bool TryGetValue<TContainer, TValue>(ref TContainer container, string name, out TValue value)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                value = default;
                return false;
            }

            var changeTracker = new ChangeTracker();
            var action = new GetValueAction<TContainer, TValue>();

            if (!PropertyBagResolver.Resolve<TContainer>().FindProperty(name, ref container, ref changeTracker, ref action))
            {
                value = default;
                return false;
            }

            value = action.DstValue;
            return action.Result == k_ResultSuccess;
        }

        /// <summary>
        /// Sets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name to set.</param>
        /// <param name="value">The value to assign to the property.</param>
        /// <param name="versionStorage">The version storage to increment if the value is changed.</param>
        /// <returns>True if the property was found and the value was set.</returns>
        public static bool TrySetValue<TContainer, TValue>(ref TContainer container, string name, TValue value, IVersionStorage versionStorage = null)
        {
            var changeTracker = new ChangeTracker(versionStorage);
            return TrySetValue(ref container, name, value, ref changeTracker);
        }


        /// <summary>
        /// Sets the value of the property with the given name for the given container.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name to set.</param>
        /// <param name="value">The value to assign to the property.</param>
        /// <param name="changeTracker">The change tracker to increment if the value changes.</param>
        /// <returns>True if the property was found and the value was set.</returns>
        public static bool TrySetValue<TContainer, TValue>(ref TContainer container, string name, TValue value, ref ChangeTracker changeTracker)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                value = default;
                return false;
            }

            var action = new SetValueAction<TContainer, TValue>
            {
                SrcValue = value
            };

            if (!PropertyBagResolver.Resolve<TContainer>().FindProperty(name, ref container, ref changeTracker, ref action))
            {
                value = default;
                return false;
            }
            
            return action.Result == k_ResultSuccess;
        }

        /// <summary>
        /// Gets the collection count for the given property.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name for the collection.</param>
        internal static int GetCount<TContainer>(ref TContainer container, string name)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                throw new InvalidOperationException($"Failed to resolve property bag for ContainerType=[{typeof(TContainer)}]");
            }

            var changeTracker = new ChangeTracker();
            var action = new GetCountAction<TContainer>();

            if (!PropertyBagResolver.Resolve<TContainer>().FindProperty(name, ref container, ref changeTracker, ref action))
            {
                throw new InvalidOperationException($"Failed to find property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }

            return action.Count;
        }
        
        /// <summary>
        /// Gets the collection count for the given property.
        /// </summary>
        /// <param name="container">The container hosting the data.</param>
        /// <param name="name">The property name for the collection.</param>
        internal static void SetCount<TContainer>(ref TContainer container, string name, int count)
        {
            var propertyBag = PropertyBagResolver.Resolve<TContainer>();

            if (null == propertyBag)
            {
                throw new InvalidOperationException($"Failed to resolve property bag for ContainerType=[{typeof(TContainer)}]");
            }

            var changeTracker = new ChangeTracker();
            var action = new SetCountAction<TContainer>(count);

            if (!PropertyBagResolver.Resolve<TContainer>().FindProperty(name, ref container, ref changeTracker, ref action))
            {
                throw new InvalidOperationException($"Failed to find property Name=[{name}] for ContainerType=[{typeof(TContainer)}]");
            }
        }
    }
}
