using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using Unity.Collections;
using Unity.Collections.LowLevel.Unsafe;
using UnityEditor.Experimental.AssetImporters;
using UnityEngine;

namespace UnityEditor.Experimental.U2D.Common.Tests
{
    public class TextureGeneratorHelperTests
    {
        public struct GenerateSpriteTestCase
        {
            public Color32[] buffer;
            public int bufferWidth;
            public int bufferHeight;
            public TextureSettings settings;
            public TextureImporterPlatformSettings platformSettings;
            public TextureSpriteSettings spriteSettings;
            public TextureAlphaSettings alphaSettings;
            public TextureMipmapSettings mipmapSettings;
            public TextureWrapSettings wrapSettings;
            public string testName;

            public override string ToString()
            {
                return testName;
            }
        }

        private static IEnumerable<TestCaseData> GenerateSpriteTestCases()
        {
            var buffer = new Color32[64 * 64];

            var settings = new TextureSettings();

            var alphaSettingsFromInput = new TextureAlphaSettings();
            alphaSettingsFromInput.alphaSource = TextureImporterAlphaSource.FromInput;
            var alphaSettingsFromGrayScale = new TextureAlphaSettings();
            alphaSettingsFromGrayScale.alphaSource = TextureImporterAlphaSource.FromGrayScale;
            var alphaSettingsNone = new TextureAlphaSettings();
            alphaSettingsNone.alphaSource = TextureImporterAlphaSource.None;

            var platformSettings = new TextureImporterPlatformSettings();
            var platformSettingsOverride = new TextureImporterPlatformSettings();
            platformSettingsOverride.overridden = true;
            platformSettingsOverride.maxTextureSize = 32;

            var spriteSettings = new TextureSpriteSettings();
            var spriteSettingsWithSprites = new TextureSpriteSettings();
            spriteSettingsWithSprites.spriteSheetData = new SpriteImportData[]
            {
                new SpriteImportData()
                {
                    name = "Sprite1",
                    rect = new Rect(0, 0, 32, 32),
                    spriteID = GUID.Generate().ToString()
                },
                new SpriteImportData()
                {
                    name = "Sprite2",
                    rect = new Rect(32, 32, 32, 32),
                    spriteID = GUID.Generate().ToString()
                },
                new SpriteImportData()
                {
                    name = "Sprite3",
                    rect = new Rect(0, 32, 32, 32),
                    spriteID = GUID.Generate().ToString()
                },
            };

            {
                GenerateSpriteTestCase testCase = new GenerateSpriteTestCase
                {
                    settings = settings,
                    platformSettings = platformSettings,
                    spriteSettings = spriteSettings,
                    bufferWidth = 64,
                    bufferHeight = 64,
                    buffer = buffer,
                    testName = "GenerateSpriteWithNoSpriteData"
                };
                yield return new TestCaseData(testCase);

                testCase.spriteSettings = spriteSettingsWithSprites;
                testCase.testName = "GenerateSpriteWithSpriteData";
                yield return new TestCaseData(testCase);
            }

            {
                GenerateSpriteTestCase testCase = new GenerateSpriteTestCase
                {
                    settings = settings,
                    platformSettings = platformSettings,
                    spriteSettings = spriteSettings,
                    bufferWidth = 64,
                    bufferHeight = 64,
                    buffer = buffer,
                    testName = "GenerateSpriteWithNoSpriteData_AlphaNone",
                    alphaSettings = alphaSettingsNone
                };
                yield return new TestCaseData(testCase);

                testCase.testName = "GenerateSpriteWithSpriteData_AlphaNone";
                testCase.spriteSettings = spriteSettingsWithSprites;
                yield return new TestCaseData(testCase);
            }

            {
                GenerateSpriteTestCase testCase = new GenerateSpriteTestCase
                {
                    settings = settings,
                    platformSettings = platformSettings,
                    spriteSettings = spriteSettings,
                    bufferWidth = 64,
                    bufferHeight = 64,
                    buffer = buffer,
                    testName = "GenerateSpriteWithNoSpriteData_AlphaGrayScale",
                    alphaSettings = alphaSettingsFromGrayScale
                };
                yield return new TestCaseData(testCase);

                testCase.testName = "GenerateSpriteWithSpriteData_AlphaGrayScale";
                testCase.spriteSettings = spriteSettingsWithSprites;
                yield return new TestCaseData(testCase);
            }

            {
                GenerateSpriteTestCase testCase = new GenerateSpriteTestCase
                {
                    settings = settings,
                    platformSettings = platformSettings,
                    spriteSettings = spriteSettings,
                    bufferWidth = 64,
                    bufferHeight = 64,
                    buffer = buffer,
                    testName = "GenerateSpriteWithNoSpriteData_AlphaFromInput",
                    alphaSettings = alphaSettingsFromInput
                };
                yield return new TestCaseData(testCase);

                testCase.testName = "GenerateSpriteWithSpriteData_AlphaFromInput";
                testCase.spriteSettings = spriteSettingsWithSprites;
                yield return new TestCaseData(testCase);
            }

            {
                GenerateSpriteTestCase testCase = new GenerateSpriteTestCase
                {
                    settings = settings,
                    platformSettings = platformSettingsOverride,
                    spriteSettings = spriteSettings,
                    bufferWidth = 64,
                    bufferHeight = 64,
                    buffer = buffer,
                    testName = "GenerateSpriteWithNoSpriteData_WithPlatformOverride",
                    alphaSettings = alphaSettingsFromInput
                };
                yield return new TestCaseData(testCase);

                testCase.testName = "GenerateSpriteWithSpriteData_WithPlatformOverride";
                testCase.spriteSettings = spriteSettingsWithSprites;
                yield return new TestCaseData(testCase);
            }
        }

        [Test, TestCaseSource("GenerateSpriteTestCases")]
        public void GenerateSpriteTests(GenerateSpriteTestCase testCase)
        {
            var nativeBuffer = new NativeArray<Color32>(testCase.buffer, Allocator.Temp);
            var output = TextureGeneratorHelper.GenerateTextureSprite(nativeBuffer, testCase.bufferWidth, testCase.bufferHeight, testCase.settings, testCase.platformSettings, testCase.spriteSettings, testCase.alphaSettings, testCase.mipmapSettings, testCase.wrapSettings);
            nativeBuffer.Dispose();
            //Verify texture is generated
            Assert.NotNull(output.texture);
            Assert.AreEqual(testCase.alphaSettings == null || testCase.alphaSettings.alphaSource != TextureImporterAlphaSource.None, output.texture.alphaIsTransparency);
            int expectedWidth = testCase.platformSettings.overridden ? Mathf.Min(testCase.bufferWidth, testCase.platformSettings.maxTextureSize) : testCase.bufferWidth;
            int expectedHeight = testCase.platformSettings.overridden ? Mathf.Min(testCase.bufferHeight, testCase.platformSettings.maxTextureSize) : testCase.bufferHeight;
            Assert.AreEqual(expectedWidth, output.texture.width);
            Assert.AreEqual(expectedHeight, output.texture.height);

            //Verify sprites
            var spriteSheetData = testCase.spriteSettings.spriteSheetData;
            if (spriteSheetData == null)
                Assert.AreEqual(0, output.sprites.Length);
            else
            {
                Assert.AreEqual(spriteSheetData.Length, output.sprites.Length);
                for (int i = 0; i < spriteSheetData.Length; ++i)
                {
                    var spriteID = new GUID(spriteSheetData[i].spriteID);
                    var sprite = output.sprites.FirstOrDefault(x => x.GetSpriteID() == spriteID);
                    Assert.NotNull(sprite);
                    Assert.AreEqual(spriteSheetData[i].name, sprite.name);
                    if (!testCase.platformSettings.overridden)
                    {
                        Assert.AreEqual(spriteSheetData[i].rect, sprite.textureRect);
                    }
                    Assert.AreEqual(output.texture, sprite.texture);
                }
            }
        }
    }
}
