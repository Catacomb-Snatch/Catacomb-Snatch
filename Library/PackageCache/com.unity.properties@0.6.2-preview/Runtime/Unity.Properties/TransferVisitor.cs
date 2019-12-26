using System;

namespace Unity.Properties
{
    /// <summary>
    /// First implementation of the "Transfer" system for properties
    ///
    /// @TODO
    ///     - Add support for instance creation.
    ///
    /// </summary>
    public struct TransferVisitor<TSourceContainer> : IPropertyVisitor
    {
        struct TransferValueAction<TDestinationProperty, TDestinationContainer, TDestinationValue> : IPropertyGetter<TSourceContainer>
            where TDestinationProperty : IProperty<TDestinationContainer, TDestinationValue>
        {
            public TDestinationProperty DstProperty;
            public TDestinationContainer DstContainer;

            public void VisitProperty<TSourceProperty, TSourceValue>(TSourceProperty srcProperty, ref TSourceContainer srcContainer, ref ChangeTracker propertyChangeTracker)
                where TSourceProperty : IProperty<TSourceContainer, TSourceValue>
            {
                var srcValue = srcProperty.GetValue(ref srcContainer);

                if (TypeConversion.TryConvert<TSourceValue, TDestinationValue>(srcValue, out var dstValue))
                {
                    if (CustomEquality.Equals(dstValue, DstProperty.GetValue(ref DstContainer)))
                    {
                        return;
                    }

                    DstProperty.SetValue(ref DstContainer, dstValue);
                    propertyChangeTracker.IncrementVersion<TDestinationProperty, TDestinationContainer, TDestinationValue>(DstProperty, ref DstContainer);
                    return;
                }

                if (DstProperty.IsContainer)
                {
                    var changeTracker = new ChangeTracker(propertyChangeTracker.VersionStorage);

                    dstValue = DstProperty.GetValue(ref DstContainer);

                    PropertyContainer.Transfer(ref dstValue, ref srcValue, ref changeTracker);

                    DstProperty.SetValue(ref DstContainer, dstValue);

                    if (changeTracker.IsChanged())
                    {
                        propertyChangeTracker.IncrementVersion<TDestinationProperty, TDestinationContainer, TDestinationValue>(DstProperty, ref DstContainer);
                    }
                }
            }

            public void VisitCollectionProperty<TSourceProperty, TSourceValue>(TSourceProperty srcProperty, ref TSourceContainer srcContainer, ref ChangeTracker changeTracker)
                where TSourceProperty : ICollectionProperty<TSourceContainer, TSourceValue>
            {
                throw new NotSupportedException();
            }
        }

        struct TransferCollectionAction<TDestinationProperty, TDestinationContainer, TDestinationValue> : IPropertyGetter<TSourceContainer>
            where TDestinationProperty : ICollectionProperty<TDestinationContainer, TDestinationValue>
        {
            struct TransferCollectionElement : ICollectionElementPropertyGetter<TSourceContainer>
            {
                public TDestinationProperty DstProperty;
                public TDestinationContainer DstContainer;
                public int Index;

                public void VisitProperty<TSourceElementProperty, TSourceElement>(TSourceElementProperty srcElementProperty, ref TSourceContainer srcContainer, ref ChangeTracker propertyChangeTracker)
                    where TSourceElementProperty : ICollectionElementProperty<TSourceContainer, TSourceElement>
                {
                    var assignment = new AssignDestinationElement<TSourceElement>
                    {
                        SrcElementValue = srcElementProperty.GetValue(ref srcContainer)
                    };

                    DstProperty.GetPropertyAtIndex(ref DstContainer, Index, ref propertyChangeTracker, assignment);
                }

                public void VisitCollectionProperty<TElementProperty, TElement>(TElementProperty property, ref TSourceContainer container, ref ChangeTracker propertyChangeTracker)
                    where TElementProperty : ICollectionProperty<TSourceContainer, TElement>, ICollectionElementProperty<TSourceContainer, TElement>
                {
                    throw new NotSupportedException();
                }
            }

            struct AssignDestinationElement<TSourceElement> : ICollectionElementPropertyGetter<TDestinationContainer>
            {
                public TSourceElement SrcElementValue;

                public void VisitProperty<TDestinationElementProperty, TDestinationElement>(TDestinationElementProperty dstElementProperty, ref TDestinationContainer dstContainer, ref ChangeTracker propertyChangeTracker)
                    where TDestinationElementProperty : ICollectionElementProperty<TDestinationContainer, TDestinationElement>
                {
                    if (TypeConversion.TryConvert<TSourceElement, TDestinationElement>(SrcElementValue, out var dstElementValue))
                    {
                        if (CustomEquality.Equals(dstElementValue, dstElementProperty.GetValue(ref dstContainer)))
                        {
                            return;
                        }

                        dstElementProperty.SetValue(ref dstContainer, dstElementValue);
                        propertyChangeTracker.IncrementVersion<TDestinationElementProperty, TDestinationContainer, TDestinationElement>(dstElementProperty, ref dstContainer);
                        return;
                    }

                    if (dstElementProperty.IsContainer)
                    {
                        var changeTracker = new ChangeTracker(propertyChangeTracker.VersionStorage);

                        dstElementValue = dstElementProperty.GetValue(ref dstContainer);

                        PropertyContainer.Transfer(ref dstElementValue, ref SrcElementValue, ref changeTracker);

                        dstElementProperty.SetValue(ref dstContainer, dstElementValue);

                        if (changeTracker.IsChanged())
                        {
                            propertyChangeTracker.IncrementVersion<TDestinationElementProperty, TDestinationContainer, TDestinationElement>(dstElementProperty, ref dstContainer);
                        }
                    }
                }

                public void VisitCollectionProperty<TElementProperty, TElement>(TElementProperty property, ref TDestinationContainer container, ref ChangeTracker propertyChangeTracker)
                    where TElementProperty : ICollectionProperty<TDestinationContainer, TElement>, ICollectionElementProperty<TDestinationContainer, TElement>
                {
                    throw new NotSupportedException();
                }
            }

            public TDestinationProperty DstProperty;
            public TDestinationContainer DstContainer;

            public void VisitProperty<TProperty, TValue>(TProperty srcProperty, ref TSourceContainer srcContainer, ref ChangeTracker changeTracker)
                where TProperty : IProperty<TSourceContainer, TValue>
            {
                throw new NotSupportedException();
            }

            public void VisitCollectionProperty<TProperty, TValue>(TProperty srcProperty, ref TSourceContainer srcContainer, ref ChangeTracker changeTracker)
                where TProperty : ICollectionProperty<TSourceContainer, TValue>
            {
                var srcCount = srcProperty.GetCount(ref srcContainer);
                var dstCount = DstProperty.GetCount(ref DstContainer);

                if (srcCount != dstCount)
                {
                    DstProperty.SetCount(ref DstContainer, srcCount);
                }

                for (var i = 0; i < srcCount; i++)
                {
                    var transfer = new TransferCollectionElement
                    {
                        DstProperty = DstProperty,
                        DstContainer = DstContainer,
                        Index = i
                    };

                    srcProperty.GetPropertyAtIndex(ref srcContainer, i, ref changeTracker, transfer);
                    DstContainer = transfer.DstContainer;
                }
            }
        }

        TSourceContainer m_SrcContainer;

        public TransferVisitor(TSourceContainer container)
        {
            m_SrcContainer = container;
        }

        public VisitStatus VisitProperty<TDestinationProperty, TDestinationContainer, TDestinationValue>(TDestinationProperty dstProperty, ref TDestinationContainer dstContainer,
            ref ChangeTracker changeTracker)
            where TDestinationProperty : IProperty<TDestinationContainer, TDestinationValue>
        {
            var sourcePropertyBag = PropertyBagResolver.Resolve<TSourceContainer>();

            if (null == sourcePropertyBag)
            {
                throw new Exception();
            }

            var transfer = new TransferValueAction<TDestinationProperty, TDestinationContainer, TDestinationValue>
            {
                DstProperty = dstProperty,
                DstContainer = dstContainer
            };

            sourcePropertyBag.FindProperty(dstProperty.GetName(), ref m_SrcContainer, ref changeTracker, ref transfer);
            dstContainer = transfer.DstContainer;

            return VisitStatus.Handled;
        }

        public VisitStatus VisitCollectionProperty<TDestinationProperty, TDestinationContainer, TDestinationValue>(TDestinationProperty dstProperty,
            ref TDestinationContainer dstContainer, ref ChangeTracker changeTracker)
            where TDestinationProperty : ICollectionProperty<TDestinationContainer, TDestinationValue>
        {
            var sourcePropertyBag = PropertyBagResolver.Resolve<TSourceContainer>();

            if (null == sourcePropertyBag)
            {
                throw new Exception();
            }

            var transfer = new TransferCollectionAction<TDestinationProperty, TDestinationContainer, TDestinationValue>
            {
                DstProperty = dstProperty,
                DstContainer = dstContainer
            };

            sourcePropertyBag.FindProperty(dstProperty.GetName(), ref m_SrcContainer, ref changeTracker, ref transfer);
            dstContainer = transfer.DstContainer;

            return VisitStatus.Handled;
        }
    }
}
