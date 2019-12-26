using System;

namespace Unity.Properties
{
    public static partial class PropertyContainer
    {
        struct TransferAbstractType<TDestination> : IContainerTypeCallback
        {
            public TDestination Destination;
            public object SourceContainer;

            public void Invoke<T>()
            {
                Visit(ref Destination, new TransferVisitor<T>((T) SourceContainer));
            }
        }

        public static void Transfer<TDestination, TSource>(TDestination destination, TSource source, IVersionStorage versionStorage = null)
            where TDestination : class
        {
            if (destination == null)
            {
                throw new ArgumentNullException(nameof(destination));
            }

            var changeTracker = new ChangeTracker(versionStorage);
            DoTransfer(ref destination, ref source, ref changeTracker);
        }

        public static void Transfer<TDestination, TSource>(ref TDestination destination, ref TSource source, IVersionStorage versionStorage = null)
        {
            if (!RuntimeTypeInfoCache<TDestination>.IsValueType() && destination == null)
            {
                throw new ArgumentNullException(nameof(destination));
            }

            var changeTracker = new ChangeTracker(versionStorage);
            DoTransfer(ref destination, ref source, ref changeTracker);
        }

        public static void Transfer<TDestination, TSource>(ref TDestination destination, ref TSource source, ref ChangeTracker changeTracker)
        {
            if (!RuntimeTypeInfoCache<TDestination>.IsValueType() && destination == null)
            {
                throw new ArgumentNullException(nameof(destination));
            }

            DoTransfer(ref destination, ref source, ref changeTracker);
        }

        static void DoTransfer<TDestination, TSource>(ref TDestination destination, ref TSource source, ref ChangeTracker changeTracker)
        {
            if (RuntimeTypeInfoCache<TSource>.IsAbstractOrInterface() || typeof(TSource) != source.GetType())
            {
                var propertyBag = PropertyBagResolver.Resolve(source.GetType());
                var action = new TransferAbstractType<TDestination>
                {
                    Destination = destination,
                    SourceContainer = source
                };
                propertyBag.Cast(ref action);
                destination = action.Destination;
            }
            else
            {
                Visit(ref destination, new TransferVisitor<TSource>(source), ref changeTracker);
            }
        }
    }
}
