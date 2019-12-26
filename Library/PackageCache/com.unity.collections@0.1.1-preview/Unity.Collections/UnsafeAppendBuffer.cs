using System;
using System.Diagnostics;

namespace Unity.Collections.LowLevel.Unsafe
{
    public unsafe struct UnsafeAppendBuffer : IDisposable
    {
        public void* Ptr;
        public int Size;
        public int Capacity;
        public readonly int Alignment;
        public readonly Allocator Allocator;

        public bool IsEmpty => Size == 0;

        [Conditional("ENABLE_UNITY_COLLECTIONS_CHECKS")]
        void CheckAlignment(int alignment)
        {
            var zeroAlignment = alignment == 0;
            var powTwoAlignment = ((alignment - 1) & alignment) == 0;
            var validAlignment = (!zeroAlignment) && powTwoAlignment;

            if (!validAlignment)
                throw new ArgumentException($"Specified alignment must be non-zero positive power of two. Requested: {alignment}");
        }

        public UnsafeAppendBuffer(int initialCapacity, int alignment, Allocator allocator)
        {
            Alignment = alignment;
            Allocator = allocator;
            Ptr = null;
            Size = 0;
            Capacity = 0;

            CheckAlignment(alignment);
            SetCapacity(initialCapacity);
        }

        public void Dispose()
        {
            UnsafeUtility.Free(Ptr, Allocator);
            Ptr = null;
            Size = 0;
            Capacity = 0;
        }

        public void Reset()
        {
            Size = 0;
        }

        public void SetCapacity(int targetCapacity)
        {
            if (targetCapacity <= Capacity)
                return;

            var newPtr = UnsafeUtility.Malloc(targetCapacity, Alignment, Allocator);
            if (Ptr != null)
            {
                UnsafeUtility.MemCpy(newPtr, Ptr, Size);
                UnsafeUtility.Free(Ptr, Allocator);
            }

            Ptr = newPtr;
            Capacity = targetCapacity;
        }

        public void Add<T>(T t) where T : struct
        {
            var structSize = UnsafeUtility.SizeOf<T>();

            SetCapacity(Size + structSize);
            UnsafeUtility.WriteArrayElement((void*)((IntPtr)Ptr + Size), 0, t);
            Size += structSize;
        }

        public void Add(void* t, int structSize)
        {
            SetCapacity(Size + structSize);
            UnsafeUtility.MemCpy((void*)((IntPtr)Ptr + Size), t, structSize);
            Size += structSize;
        }

        public T Pop<T>() where T : struct
        {
            int structSize = UnsafeUtility.SizeOf<T>();
            long ptr = (long)Ptr;
            long size = (long)Size;
            long addr = ptr + size - (long)structSize;

            var t = UnsafeUtility.ReadArrayElement<T>((void*)addr, 0);
            Size -= structSize;
            return t;
        }

        public void Pop(void* t, int structSize)
        {
            long ptr = (long)Ptr;
            long size = (long)Size;
            long addr = ptr + size - (long)structSize;

            UnsafeUtility.MemCpy(t, (void*)addr, structSize);
            Size -= structSize;
        }

        public Reader AsReader()
        {
            return new Reader(ref this);
        }

        public unsafe struct Reader
        {
            public readonly void* Ptr;
            public readonly int Size;
            public int Offset;

            public bool EndOfBuffer => Offset == Size;

            public Reader(ref UnsafeAppendBuffer buffer)
            {
                Ptr = buffer.Ptr;
                Size = buffer.Size;
                Offset = 0;
            }

            [Conditional("ENABLE_UNITY_COLLECTIONS_CHECKS")]
            void CheckBounds(int structSize)
            {
                if (Offset + structSize > Size)
                    throw new ArgumentException("Requested value outside bounds of UnsafeAppendOnlyBuffer. Remaining bytes: {Buffer->Size - m_Offset} Requested: {structSize}");
            }

            public T ReadNext<T>() where T : struct
            {
                var structSize = UnsafeUtility.SizeOf<T>();
                CheckBounds(structSize);

                T value = UnsafeUtility.ReadArrayElement<T>((void*)((IntPtr)Ptr + Offset), 0);
                Offset += structSize;
                return value;
            }

            public void* ReadNext(int structSize)
            {
                CheckBounds(structSize);

                var value = (void*)((IntPtr)Ptr + Offset);
                Offset += structSize;
                return value;
            }
        }
    }
}
