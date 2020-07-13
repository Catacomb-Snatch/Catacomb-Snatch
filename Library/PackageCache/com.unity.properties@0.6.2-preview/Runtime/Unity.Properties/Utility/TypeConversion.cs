using System;
using Unity.Collections.LowLevel.Unsafe;

namespace Unity.Properties
{
    public static class TypeConversion
    {
        public struct Converter<TSource, TDestination>
        {
            public delegate TDestination ConvertDelegate(TSource value);

            // ReSharper disable once MemberHidesStaticFromOuterClass
            public static ConvertDelegate Convert;
        }

        static TypeConversion()
        {
            PrimitiveConverters.Register();
        }

        public static void Register<TSource, TDestination>(Converter<TSource, TDestination>.ConvertDelegate converter)
        {
            Converter<TSource, TDestination>.Convert = converter;
        }

        public static bool HasConverter<TSource, TDestination>()
        {
            return null != Converter<TSource, TDestination>.Convert;
        }

        public static TDestination Convert<TSource, TDestination>(TSource value)
        {
            if (null == Converter<TSource, TDestination>.Convert)
            {
                throw new Exception($"TypeConversion no converter has been registered for SrcType=[{typeof(TSource)}] to DstType=[{typeof(TDestination)}]");
            }

            return Converter<TSource, TDestination>.Convert(value);
        }

        public static unsafe bool TryConvert<TSource, TDestination>(TSource source, out TDestination destination)
        {
            if (null == Converter<TSource, TDestination>.Convert)
            {
                if (typeof(TSource) == typeof(TDestination))
                {
                    if (UnsafeUtility.IsBlittable(typeof(TSource)))
                    {
                        destination = default;
                        var ptr = System.Runtime.CompilerServices.Unsafe.AsPointer(ref source);
                        System.Runtime.CompilerServices.Unsafe.Copy(ref destination, ptr);
                        return true;
                    }

                    Register<TSource, TSource>(value => value);
                    destination = Convert<TSource, TDestination>(source);
                    return true;
                }

                if (typeof(TDestination).IsEnum)
                {
                    if (typeof(TSource) == typeof(string))
                    {
                        destination = (TDestination) Enum.Parse(typeof(TDestination), (string) (object) source);
                        return true;
                    }

                    // Boxing :(
                    destination = (TDestination) (object)Convert<TSource, int>(source);
                    return true;
                }

                destination = default;
                return false;
            }

            destination = Converter<TSource, TDestination>.Convert(source);
            return true;
        }

        static class PrimitiveConverters
        {
            public static void Register()
            {
                // signed integral types
                RegisterInt8Converters();
                RegisterInt16Converters();
                RegisterInt32Converters();
                RegisterInt64Converters();

                // unsigned integral types
                RegisterUInt8Converters();
                RegisterUInt16Converters();
                RegisterUInt32Converters();
                RegisterUInt64Converters();

                // floating point types
                RegisterFloat32Converters();
                RegisterFloat64Converters();

                // .net types
                RegisterBooleanConverters();
                RegisterCharConverters();
                RegisterStringConverters();
                RegisterObjectConverters();

                // support System.Guid by default
                TypeConversion.Register<string, Guid>(g => new Guid(g));
            }

            static void RegisterInt8Converters()
            {
                Converter<sbyte, char>.Convert = v => (char) v;
                Converter<sbyte, bool>.Convert = v => v != 0;
                Converter<sbyte, sbyte>.Convert = v => (sbyte) v;
                Converter<sbyte, short>.Convert = v => (short) v;
                Converter<sbyte, int>.Convert = v => (int) v;
                Converter<sbyte, long>.Convert = v => (long) v;
                Converter<sbyte, byte>.Convert = v => (byte) v;
                Converter<sbyte, ushort>.Convert = v => (ushort) v;
                Converter<sbyte, uint>.Convert = v => (uint) v;
                Converter<sbyte, ulong>.Convert = v => (ulong) v;
                Converter<sbyte, float>.Convert = v => (float) v;
                Converter<sbyte, double>.Convert = v => (double) v;
            }

            static void RegisterInt16Converters()
            {
                Converter<short, char>.Convert = v => (char) v;
                Converter<short, bool>.Convert = v => v != 0;
                Converter<short, sbyte>.Convert = v => (sbyte) v;
                Converter<short, short>.Convert = v => (short) v;
                Converter<short, int>.Convert = v => (int) v;
                Converter<short, long>.Convert = v => (long) v;
                Converter<short, byte>.Convert = v => (byte) v;
                Converter<short, ushort>.Convert = v => (ushort) v;
                Converter<short, uint>.Convert = v => (uint) v;
                Converter<short, ulong>.Convert = v => (ulong) v;
                Converter<short, float>.Convert = v => (float) v;
                Converter<short, double>.Convert = v => (double) v;
            }

            static void RegisterInt32Converters()
            {
                Converter<int, char>.Convert = v => (char) v;
                Converter<int, bool>.Convert = v => v != 0;
                Converter<int, sbyte>.Convert = v => (sbyte) v;
                Converter<int, short>.Convert = v => (short) v;
                Converter<int, int>.Convert = v => (int) v;
                Converter<int, long>.Convert = v => (long) v;
                Converter<int, byte>.Convert = v => (byte) v;
                Converter<int, ushort>.Convert = v => (ushort) v;
                Converter<int, uint>.Convert = v => (uint) v;
                Converter<int, ulong>.Convert = v => (ulong) v;
                Converter<int, float>.Convert = v => (float) v;
                Converter<int, double>.Convert = v => (double) v;
            }

            static void RegisterInt64Converters()
            {
                Converter<long, char>.Convert = v => (char) v;
                Converter<long, bool>.Convert = v => v != 0;
                Converter<long, sbyte>.Convert = v => (sbyte) v;
                Converter<long, short>.Convert = v => (short) v;
                Converter<long, int>.Convert = v => (int) v;
                Converter<long, long>.Convert = v => (long) v;
                Converter<long, byte>.Convert = v => (byte) v;
                Converter<long, ushort>.Convert = v => (ushort) v;
                Converter<long, uint>.Convert = v => (uint) v;
                Converter<long, ulong>.Convert = v => (ulong) v;
                Converter<long, float>.Convert = v => (float) v;
                Converter<long, double>.Convert = v => (double) v;
            }

            static void RegisterUInt8Converters()
            {
                Converter<byte, char>.Convert = v => (char) v;
                Converter<byte, bool>.Convert = v => v != 0;
                Converter<byte, sbyte>.Convert = v => (sbyte) v;
                Converter<byte, short>.Convert = v => (short) v;
                Converter<byte, int>.Convert = v => (int) v;
                Converter<byte, long>.Convert = v => (long) v;
                Converter<byte, byte>.Convert = v => (byte) v;
                Converter<byte, ushort>.Convert = v => (ushort) v;
                Converter<byte, uint>.Convert = v => (uint) v;
                Converter<byte, ulong>.Convert = v => (ulong) v;
                Converter<byte, float>.Convert = v => (float) v;
                Converter<byte, double>.Convert = v => (double) v;
            }

            static void RegisterUInt16Converters()
            {
                Converter<ushort, char>.Convert = v => (char) v;
                Converter<ushort, bool>.Convert = v => v != 0;
                Converter<ushort, sbyte>.Convert = v => (sbyte) v;
                Converter<ushort, short>.Convert = v => (short) v;
                Converter<ushort, int>.Convert = v => (int) v;
                Converter<ushort, long>.Convert = v => (long) v;
                Converter<ushort, byte>.Convert = v => (byte) v;
                Converter<ushort, ushort>.Convert = v => (ushort) v;
                Converter<ushort, uint>.Convert = v => (uint) v;
                Converter<ushort, ulong>.Convert = v => (ulong) v;
                Converter<ushort, float>.Convert = v => (float) v;
                Converter<ushort, double>.Convert = v => (double) v;
            }

            static void RegisterUInt32Converters()
            {
                Converter<uint, char>.Convert = v => (char) v;
                Converter<uint, bool>.Convert = v => v != 0;
                Converter<uint, sbyte>.Convert = v => (sbyte) v;
                Converter<uint, short>.Convert = v => (short) v;
                Converter<uint, int>.Convert = v => (int) v;
                Converter<uint, long>.Convert = v => (long) v;
                Converter<uint, byte>.Convert = v => (byte) v;
                Converter<uint, ushort>.Convert = v => (ushort) v;
                Converter<uint, uint>.Convert = v => (uint) v;
                Converter<uint, ulong>.Convert = v => (ulong) v;
                Converter<uint, float>.Convert = v => (float) v;
                Converter<uint, double>.Convert = v => (double) v;
            }

            static void RegisterUInt64Converters()
            {
                Converter<ulong, char>.Convert = v => (char) v;
                Converter<ulong, bool>.Convert = v => v != 0;
                Converter<ulong, sbyte>.Convert = v => (sbyte) v;
                Converter<ulong, short>.Convert = v => (short) v;
                Converter<ulong, int>.Convert = v => (int) v;
                Converter<ulong, long>.Convert = v => (long) v;
                Converter<ulong, byte>.Convert = v => (byte) v;
                Converter<ulong, ushort>.Convert = v => (ushort) v;
                Converter<ulong, uint>.Convert = v => (uint) v;
                Converter<ulong, ulong>.Convert = v => (ulong) v;
                Converter<ulong, float>.Convert = v => (float) v;
                Converter<ulong, double>.Convert = v => (double) v;
            }

            static void RegisterFloat32Converters()
            {
                Converter<float, char>.Convert = v => (char) v;
                Converter<float, bool>.Convert = v => Math.Abs(v) > float.Epsilon;
                Converter<float, sbyte>.Convert = v => (sbyte) v;
                Converter<float, short>.Convert = v => (short) v;
                Converter<float, int>.Convert = v => (int) v;
                Converter<float, long>.Convert = v => (long) v;
                Converter<float, byte>.Convert = v => (byte) v;
                Converter<float, ushort>.Convert = v => (ushort) v;
                Converter<float, uint>.Convert = v => (uint) v;
                Converter<float, ulong>.Convert = v => (ulong) v;
                Converter<float, float>.Convert = v => (float) v;
                Converter<float, double>.Convert = v => (double) v;
            }

            static void RegisterFloat64Converters()
            {
                Converter<double, char>.Convert = v => (char) v;
                Converter<double, bool>.Convert = v => Math.Abs(v) > double.Epsilon;
                Converter<double, sbyte>.Convert = v => (sbyte) v;
                Converter<double, short>.Convert = v => (short) v;
                Converter<double, int>.Convert = v => (int) v;
                Converter<double, long>.Convert = v => (long) v;
                Converter<double, byte>.Convert = v => (byte) v;
                Converter<double, ushort>.Convert = v => (ushort) v;
                Converter<double, uint>.Convert = v => (uint) v;
                Converter<double, ulong>.Convert = v => (ulong) v;
                Converter<double, float>.Convert = v => (float) v;
                Converter<double, double>.Convert = v => (double) v;
            }

            static void RegisterBooleanConverters()
            {
                Converter<bool, char>.Convert = v => v ? (char) 1 : (char) 0;
                Converter<bool, bool>.Convert = v => v;
                Converter<bool, sbyte>.Convert = v => v ? (sbyte) 1 : (sbyte) 0;
                Converter<bool, short>.Convert = v => v ? (short) 1 : (short) 0;
                Converter<bool, int>.Convert = v => v ? (int) 1 : (int) 0;
                Converter<bool, long>.Convert = v => v ? (long) 1 : (long) 0;
                Converter<bool, byte>.Convert = v => v ? (byte) 1 : (byte) 0;
                Converter<bool, ushort>.Convert = v => v ? (ushort) 1 : (ushort) 0;
                Converter<bool, uint>.Convert = v => v ? (uint) 1 : (uint) 0;
                Converter<bool, ulong>.Convert = v => v ? (ulong) 1 : (ulong) 0;
                Converter<bool, float>.Convert = v => v ? (float) 1 : (float) 0;
                Converter<bool, double>.Convert = v => v ? (double) 1 : (double) 0;
            }

            static void RegisterCharConverters()
            {
                Converter<string, char>.Convert = v =>
                {
                    if (v.Length != 1)
                    {
                        throw new Exception("Not a valid char");
                    }

                    return v[0];
                };
                Converter<char, char>.Convert = v => v;
                Converter<char, bool>.Convert = v => v != (char) 0;
                Converter<char, sbyte>.Convert = v => (sbyte) v;
                Converter<char, short>.Convert = v => (short) v;
                Converter<char, int>.Convert = v => (int) v;
                Converter<char, long>.Convert = v => (long) v;
                Converter<char, byte>.Convert = v => (byte) v;
                Converter<char, ushort>.Convert = v => (ushort) v;
                Converter<char, uint>.Convert = v => (uint) v;
                Converter<char, ulong>.Convert = v => (ulong) v;
                Converter<char, float>.Convert = v => (float) v;
                Converter<char, double>.Convert = v => (double) v;
            }

            static void RegisterStringConverters()
            {
                Converter<string, string>.Convert = v => v;
                Converter<string, char>.Convert = v =>
                {
                    char.TryParse(v, out var r);
                    return r;
                };
                Converter<string, bool>.Convert = v =>
                {
                    bool.TryParse(v, out var r);
                    return r;
                };
                Converter<string, sbyte>.Convert = v =>
                {
                    sbyte.TryParse(v, out var r);
                    return r;
                };
                Converter<string, short>.Convert = v =>
                {
                    short.TryParse(v, out var r);
                    return r;
                };
                Converter<string, int>.Convert = v =>
                {
                    int.TryParse(v, out var r);
                    return r;
                };
                Converter<string, long>.Convert = v =>
                {
                    long.TryParse(v, out var r);
                    return r;
                };
                Converter<string, byte>.Convert = v =>
                {
                    byte.TryParse(v, out var r);
                    return r;
                };
                Converter<string, ushort>.Convert = v =>
                {
                    ushort.TryParse(v, out var r);
                    return r;
                };
                Converter<string, uint>.Convert = v =>
                {
                    uint.TryParse(v, out var r);
                    return r;
                };
                Converter<string, ulong>.Convert = v =>
                {
                    ulong.TryParse(v, out var r);
                    return r;
                };
                Converter<string, float>.Convert = v =>
                {
                    float.TryParse(v, out var r);
                    return r;
                };
                Converter<string, double>.Convert = v =>
                {
                    double.TryParse(v, out var r);
                    return r;
                };
            }

            static void RegisterObjectConverters()
            {
                Converter<object, char>.Convert = v => v is char value ? value : default;
                Converter<object, bool>.Convert = v => v is bool value ? value : default;
                Converter<object, sbyte>.Convert = v => v is sbyte value ? value : default;
                Converter<object, short>.Convert = v => v is short value ? value : default;
                Converter<object, int>.Convert = v => v is int value ? value : default;
                Converter<object, long>.Convert = v => v is long value ? value : default;
                Converter<object, byte>.Convert = v => v is byte value ? value : default;
                Converter<object, ushort>.Convert = v => v is ushort value ? value : default;
                Converter<object, uint>.Convert = v => v is uint value ? value : default;
                Converter<object, ulong>.Convert = v => v is ulong value ? value : default;
                Converter<object, float>.Convert = v => v is float value ? value : default;
                Converter<object, double>.Convert = v => v is double value ? value : default;
            }
        }
    }
}
