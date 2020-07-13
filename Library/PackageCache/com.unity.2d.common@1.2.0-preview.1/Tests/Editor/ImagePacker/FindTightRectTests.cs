using UnityEngine;
using NUnit.Framework;
using System.Collections.Generic;
using Unity.Collections;

namespace UnityEditor.Experimental.U2D.Common.Tests
{
    internal class FindTightRectTests
    {
        private static IEnumerable<TestCaseData> TrimAlphaTestCases()
        {
            var buffer = new Color32[64 * 64];
            yield return new TestCaseData(buffer, 64, 64, new RectInt(64, 64, 0, 0));

            buffer = new Color32[64 * 64];
            buffer[16 * 64  + 16] = new Color32(255, 255, 255, 255);
            buffer[16 * 64  + 17] = new Color32(255, 255, 255, 255);
            yield return new TestCaseData(buffer, 64, 64, new RectInt(16, 16, 2, 1));

            buffer = new Color32[64 * 64];
            buffer[16 * 64 + 16] = new Color32(255, 255, 255, 255);
            buffer[31 * 64 + 31] = new Color32(255, 255, 255, 255);
            yield return new TestCaseData(buffer, 64, 64, new RectInt(16, 16, 16, 16));

            buffer = new Color32[64 * 64];
            buffer[16 * 64 + 16] = new Color32(255, 255, 255, 255);
            buffer[18 * 64 + 17] = new Color32(255, 255, 255, 255);
            buffer[18 * 64 + 18] = new Color32(255, 255, 255, 255);
            buffer[31 * 64 + 31] = new Color32(255, 255, 255, 255);
            yield return new TestCaseData(buffer, 64, 64, new RectInt(16, 16, 16, 16));
        }

        [Test, TestCaseSource("TrimAlphaTestCases")]
        public void TrimAlphaParametricTests(Color32[] buffer, int width, int height, RectInt expectedOutput)
        {
            var nativeArrayBuffer = new NativeArray<Color32>(buffer, Allocator.Temp);
            var rectOut = FindTightRectJob.Execute(new[] { nativeArrayBuffer }, width, height);
            nativeArrayBuffer.Dispose();
            Assert.AreEqual(expectedOutput, rectOut[0]);
        }
    }
}
