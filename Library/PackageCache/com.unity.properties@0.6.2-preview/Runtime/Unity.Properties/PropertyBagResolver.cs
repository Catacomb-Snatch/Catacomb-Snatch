using System;
using System.Collections.Generic;
using Unity.Properties.Reflection;

namespace Unity.Properties
{
    public static class PropertyBagResolver
    {
        /// <summary>
        /// Static <see cref="IPropertyBag"/> lookup for strongly typed containers.
        /// </summary>
        /// <typeparam name="TContainer">The host container type.</typeparam>
        struct Lookup<TContainer>
        {
            public static IPropertyBag<TContainer> PropertyBag;
        }

        /// <summary>
        /// Dynamic lookup by <see cref="System.Type"/> for property bags.
        /// </summary>
        static readonly Dictionary<Type, IPropertyBag> s_PropertyBagByType = new Dictionary<Type, IPropertyBag>();
        
        public static ReflectedPropertyBagProvider ReflectedPropertyBagProvider { get; } = new ReflectedPropertyBagProvider();

        public static void Register<TContainer>(IPropertyBag<TContainer> propertyBag)
        {
            Lookup<TContainer>.PropertyBag = propertyBag;
            s_PropertyBagByType[typeof(TContainer)] = propertyBag;
        }
        
        public static void Register(Type type, IPropertyBag propertyBag)
        {
            s_PropertyBagByType[type] = propertyBag;
        }

        public static IPropertyBag<TContainer> Resolve<TContainer>()
        {
            var propertyBag = Lookup<TContainer>.PropertyBag;

            if (null != propertyBag)
            {
                return propertyBag;
            }

            s_PropertyBagByType.TryGetValue(typeof(TContainer), out var untypedPropertyBag);

            if (null != untypedPropertyBag)
            {
                return (IPropertyBag<TContainer>) untypedPropertyBag;
            }

            if (TryGeneratePropertyBag(out propertyBag))
            {
                Register(propertyBag);
            }

            return propertyBag;
        }

        public static IPropertyBag Resolve(Type type)
        {
            s_PropertyBagByType.TryGetValue(type, out var propertyBag);

            if (null == propertyBag)
            {
                if (TryGeneratePropertyBag(type, out propertyBag))
                {
                    s_PropertyBagByType.Add(type, propertyBag);
                }
            }

            return propertyBag;
        }

        static bool TryGeneratePropertyBag<TContainer>(out IPropertyBag<TContainer> propertyBag)
        {
            // Try to use reflection if present.
            if (null != ReflectedPropertyBagProvider) 
            {
                propertyBag = ReflectedPropertyBagProvider.Generate<TContainer>();

                if (null != propertyBag)
                {
                    return true;
                }
            }

            propertyBag = null;
            return false;
        }

        static bool TryGeneratePropertyBag(Type type, out IPropertyBag propertyBag)
        {
            // Try to use reflection if present.
            if (null != ReflectedPropertyBagProvider) 
            {
                propertyBag = ReflectedPropertyBagProvider.Generate(type);

                if (null != propertyBag)
                {
                    return true;
                }
            }

            propertyBag = null;
            return false;
        }
    }
}
