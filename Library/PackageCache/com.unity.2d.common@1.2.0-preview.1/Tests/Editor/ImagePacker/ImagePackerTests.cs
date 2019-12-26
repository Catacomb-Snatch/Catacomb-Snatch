using UnityEngine;
using NUnit.Framework;
using System.Collections.Generic;
using System;
using System.IO;
using System.Linq;
using Unity.Collections;

namespace UnityEditor.Experimental.U2D.Common.Tests
{
    internal class ImagePackerTests
    {
        [TestCase(1, ExpectedResult = 1)]
        [TestCase(2, ExpectedResult = 2)]
        [TestCase(3, ExpectedResult = 4)]
        [TestCase(4, ExpectedResult = 4)]
        [TestCase(5, ExpectedResult = 8)]
        [TestCase(6, ExpectedResult = 8)]
        [TestCase(7, ExpectedResult = 8)]
        [TestCase(8, ExpectedResult = 8)]
        [TestCase(9, ExpectedResult = 16)]
        public int NextPowerOfTwoTest(int value)
        {
            return (int)ImagePacker.NextPowerOfTwo((ulong)value);
        }

        private static IEnumerable<TestCaseData> PackSpriteTestCases()
        {
            yield return new TestCaseData(new[] { new Vector2Int(64, 64) }, 0,
                new[]
            {
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 0} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 1} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 2} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 3} ,
            },
                4, 0
                );

            yield return new TestCaseData(new[] { new Vector2Int(64, 64) }, 0,
                new[]
            {
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 0} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 1} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 2} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 3} ,
            },
                4, 0
                );

            yield return new TestCaseData(new[] { new Vector2Int(64, 64), new Vector2Int(128, 64) }, 0,
                new[]
            {
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 64, 64), index = 0} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 1} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 2} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 3} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 4} ,
            },
                5, 1
                );

            yield return new TestCaseData(new[] { new Vector2Int(64, 64), new Vector2Int(128, 64) }, 0,
                new[]
            {
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 0, 0), index = 0} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 1} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 16, 16), index = 2} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 3} ,
                new ImagePacker.ImagePackRect() {rect = new RectInt(0, 0, 32, 32), index = 4} ,
            },
                5, 0
                );

            yield return new TestCaseData(new Vector2Int[0] {}, 0, new ImagePacker.ImagePackRect[0], 5, 0);
        }

        [Test, TestCaseSource("PackSpriteTestCases")]
        public void ImagePackRectSortTests(Vector2Int[] atlasSize, int padding, ImagePacker.ImagePackRect[] packRect, int expectedPass, int expectedFail)
        {
            Array.Sort(packRect);
            for (int i = 0, j = 1; i < packRect.Length - 1; ++i, ++j)
            {
                var area1 = packRect[i].rect.width * packRect[i].rect.height;
                var area2 = packRect[j].rect.width * packRect[j].rect.height;
                Assert.IsTrue(area1 > area2 || (area1 <= area2 && packRect[i].index < packRect[j].index));
            }
        }

        [Test, TestCaseSource("PackSpriteTestCases")]
        public void RectPackTests(Vector2Int[] atlasSize, int padding,
            ImagePacker.ImagePackRect[] packRect, int expectedPass, int expectedFail)
        {
            if (atlasSize.Length == 0)
                return;
            Array.Sort(packRect);
            var root = new ImagePackNode();
            root.rect = new RectInt(0, 0, atlasSize[0].x, atlasSize[0].y);
            int passed = 0, failed = 0;
            int atlasSizeIndex = 1;
            for (int i = 0; i < packRect.Length; ++i)
            {
                var rect = packRect[i];
                var result = root.Insert(rect, padding);
                if (result)
                    ++passed;
                else
                {
                    ++failed;
                    if (atlasSizeIndex < atlasSize.Length)
                    {
                        int a, b;
                        root.AdjustSize(root.rect.width, root.rect.height, atlasSize[atlasSizeIndex].x, atlasSize[atlasSizeIndex].y, out a , out b);
                        ++atlasSizeIndex;
                        --i;
                    }
                }
            }
            Assert.AreEqual(expectedPass, passed);
            Assert.AreEqual(expectedFail, failed);
        }

        private static IEnumerable<TestCaseData> PackWithNoBufferMosaicTestCases()
        {
            var testRects = new[]
            {
                new RectInt(0, 0, 0, 0),
                new RectInt(0, 0, 16, 16),
                new RectInt(0, 0, 16, 16),
                new RectInt(0, 0, 32, 32),
                new RectInt(0, 0, 32, 32),
                new RectInt(0, 0, 8, 8),
                new RectInt(0, 0, 8, 8),
                new RectInt(0, 0, 8, 8),
                new RectInt(0, 0, 16, 16),
            };
            yield return new TestCaseData(testRects, 0, new Vector2Int(64, 64));
            yield return new TestCaseData(testRects, 2, new Vector2Int(64, 128));
            yield return new TestCaseData(testRects, 4, new Vector2Int(64, 128));
            yield return new TestCaseData(new RectInt[0], 0, new Vector2Int(0, 0));
        }

        [Test, TestCaseSource("PackWithNoBufferMosaicTestCases")]
        public void PackWithNoBufferMosaicTests(RectInt[] rects, int padding, Vector2Int expectedSize)
        {
            RectInt[] packedRect;
            int packedWidth, packedHeight;
            ImagePacker.Pack(rects, padding, out packedRect, out packedWidth, out packedHeight);
            Assert.AreEqual(expectedSize.x, packedWidth);
            Assert.AreEqual(expectedSize.y, packedHeight);
            Assert.AreEqual(rects.Length, packedRect.Length);

            // Ensure all rects exists
            for (int i = 0; i < packedRect.Length; ++i)
            {
                // Ensure rect doesn't overlap with other rects
                RectInt testRect = packedRect[i];
                for (int k = i + 1; k < packedRect.Length; ++k)
                {
                    RectInt other = packedRect[k];
                    var contains = !((other.xMax + padding <= testRect.xMin ||
                                      other.yMax + padding <= testRect.yMin) ||
                                     (other.xMin >= testRect.xMax + padding ||
                                      other.yMin >= testRect.yMax + padding));
                    Assert.IsFalse(contains);
                }

                // Ensure rects has the correct padding
                Assert.AreEqual(rects[i].width, testRect.width);
                Assert.AreEqual(rects[i].height, testRect.height);
            }
        }

        [Test, TestCaseSource("PackWithNoBufferMosaicTestCases")]
        public void PackWithBufferMosaicTests(RectInt[] rects, int padding, Vector2Int expectedSize)
        {
            RectInt[] packedRect;
            int packedWidth, packedHeight;

            // Find the largest buffer
            int maxWidth = -1, maxHeight = -1;
            for (int i = 0; i < rects.Length; ++i)
            {
                if (rects[i].width > maxWidth)
                    maxWidth = rects[i].width;
                if (rects[i].height > maxHeight)
                    maxHeight = rects[i].height;
            }

            // Setup color buffers
            NativeArray<Color32>[] buffers = new NativeArray<Color32>[rects.Length];
            for (int i = 0; i < rects.Length; ++i)
            {
                buffers[i] = new NativeArray<Color32>(maxWidth * maxHeight, Allocator.Temp);
                for (int j = 0; j < maxHeight; ++j)
                {
                    for (int k = 0; k < maxWidth; ++k)
                    {
                        int bufferIndex = (j * maxWidth  + k);
                        if (k >= rects[i].width || j >= rects[i].height)
                            buffers[i][bufferIndex] = new Color32(0, 0, 0, 0);
                        else
                        {
                            buffers[i][bufferIndex] = new Color32((byte)i , (byte)i , (byte)i, 255);
                        }
                    }
                }
            }

            NativeArray<Color32> packedBuffer;
            Vector2Int[] uvTransform;
            ImagePacker.Pack(buffers, maxWidth, maxHeight, padding, out packedBuffer, out packedWidth, out packedHeight, out packedRect, out uvTransform);
            Assert.AreEqual(expectedSize.x, packedWidth);
            Assert.AreEqual(expectedSize.y, packedHeight);
            Assert.AreEqual(rects.Length, packedRect.Length);

            // Ensure all rects exists
            int bytesPerRow = packedWidth;
            for (int i = 0; i < packedRect.Length; ++i)
            {
                // Ensure rect doesn't overlap with other rects
                RectInt testRect = packedRect[i];
                for (int k = i + 1; k < packedRect.Length; ++k)
                {
                    RectInt other = packedRect[k];
                    var contains = !((other.xMax + padding <= testRect.xMin ||
                                      other.yMax + padding <= testRect.yMin) ||
                                     (other.xMin >= testRect.xMax + padding ||
                                      other.yMin >= testRect.yMax + padding));
                    Assert.IsFalse(contains);
                }

                // Ensure rects has the correct padding
                Assert.AreEqual(rects[i].width, testRect.width);
                Assert.AreEqual(rects[i].height, testRect.height);

                // Ensure buffers are blitted to the correct place. We will just sample min, max and center point
                if (testRect.width > 0 && testRect.height > 0)
                {
                    Assert.IsTrue(PixelColorEqual(packedBuffer, packedWidth, bytesPerRow, testRect.xMin + padding, testRect.yMin, new Color32((byte)i, (byte)i, (byte)i, 255)));
                    Assert.IsTrue(PixelColorEqual(packedBuffer, packedWidth, bytesPerRow, testRect.xMax - 1, testRect.yMax - 1, new Color32((byte)i, (byte)i, (byte)i, 255)));
                    Assert.IsTrue(PixelColorEqual(packedBuffer, packedWidth, bytesPerRow, (int)testRect.center.x - 1, (int)testRect.center.y - 1, new Color32((byte)i, (byte)i, (byte)i, 255)));
                }
            }

            packedBuffer.Dispose();
            for (int i = 0; i < buffers.Length; ++i)
                buffers[i].Dispose();
        }

        bool PixelColorEqual(NativeArray<Color32> buffer, int bufferWidth, int bytesPerRow, int x, int y, Color32 expected)
        {
            int bytesPerPixel = bytesPerRow / bufferWidth;

            return buffer[y * bytesPerRow + x * bytesPerPixel].r == expected.r &&
                buffer[y * bytesPerRow + x * bytesPerPixel].g == expected.g &&
                buffer[y * bytesPerRow + x * bytesPerPixel].b == expected.b &&
                buffer[y * bytesPerRow + x * bytesPerPixel].a == expected.a;
        }
    }
}
