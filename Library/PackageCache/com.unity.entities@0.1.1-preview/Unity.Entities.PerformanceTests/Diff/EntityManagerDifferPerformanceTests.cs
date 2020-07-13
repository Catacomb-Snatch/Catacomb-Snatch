using NUnit.Framework;
using Unity.Collections;
using Unity.Entities.Tests;
using Unity.PerformanceTesting;

namespace Unity.Entities.PerformanceTests
{
    [TestFixture]
    [Category("Performance")]
    class EntityManagerDifferPerformanceTests : EntityManagerDiffPerformanceTestFixture
    {
        /// <summary>
        /// Performance test for getting changes with all options.
        /// </summary>
#if UNITY_2019_2_OR_NEWER
        [Test, Performance]
#else
        [PerformanceTest]
#endif
        [TestCase(100)]
        [TestCase(1000)]
        public void PerformanceTest_EntityManagerDiffer_GetChanges_DefaultOptions(int entityCount)
        {
            CreateEntitiesWithMockComponentData(SrcEntityManager, entityCount, typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestSharedComp));

            Measure.Method(() =>
                   {
                       using (var differ = new EntityManagerDiffer(SrcWorld, Allocator.TempJob))
                       {
                           using (differ.GetChanges(EntityManagerDifferOptions.Default, Allocator.TempJob))
                           {
                           }
                       }
                   })
                   .Definition("EntityManagerDiffer")
                   .WarmupCount(1)
                   .MeasurementCount(100)
                   .Run();
        }
        
        /// <summary>
        /// Test to generate a forward change set for a given number of entities.
        /// </summary>
        /// <param name="entityCount"></param>
#if UNITY_2019_2_OR_NEWER
        [Test, Performance]
#else
        [PerformanceTest]
#endif
        [TestCase(1000)]
        public void PerformanceTest_EntityManagerDiffer_GetChanges_ForwardChangesOnly(int entityCount)
        {
            CreateEntitiesWithMockComponentData(SrcEntityManager, entityCount, typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestSharedComp));

            Measure.Method(() =>
                   {
                       using (var differ = new EntityManagerDiffer(SrcWorld, Allocator.TempJob))
                       {
                           using (differ.GetChanges(EntityManagerDifferOptions.IncludeForwardChangeSet, Allocator.TempJob))
                           {
                           }
                       }
                   })
                   .Definition("EntityManagerDiffer")
                   .WarmupCount(1)
                   .MeasurementCount(100)
                   .Run();
        }

        /// <summary>
        /// Performance test for fast forwarding the shadow world without generating changes.
        /// </summary>
#if UNITY_2019_2_OR_NEWER
        [Test, Performance]
#else
        [PerformanceTest]
#endif
        [TestCase(1000)]
        public void PerformanceTest_EntityManagerDiffer_GetChanges_FastForwardOnly(int entityCount)
        {
            CreateEntitiesWithMockComponentData(SrcEntityManager, entityCount, typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestSharedComp));

            Measure.Method(() =>
                   {
                       using (var differ = new EntityManagerDiffer(SrcWorld, Allocator.TempJob))
                       {
                           using (differ.GetChanges(EntityManagerDifferOptions.FastForwardShadowWorld, Allocator.TempJob))
                           {
                           }
                       }
                   })
                   .Definition("EntityManagerDiffer")
                   .WarmupCount(1)
                   .MeasurementCount(100)
                   .Run();
        }
        
        /// <summary>
        /// Test case for an incremental update where no changes happen. This is the best case scenario where we can skip all work.
        /// </summary>
#if UNITY_2019_2_OR_NEWER
        [Test, Performance]
#else
        [PerformanceTest]
#endif
        [TestCase(1000)]
        [TestCase(10000)]
        [TestCase(100000)]
        public void PerformanceTest_EntityManagerDiffer_GetChanges_NoChanges(int entityCount)
        {
            CreateEntitiesWithMockComponentData(SrcEntityManager, entityCount, typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestSharedComp));

            using (var differ = new EntityManagerDiffer(SrcWorld, Allocator.TempJob))
            {
                // Fast forward the shadow world
                using (differ.GetChanges(EntityManagerDifferOptions.FastForwardShadowWorld, Allocator.TempJob))
                {
                    
                }

                Measure.Method(() =>
                       {
                           // Get changes with all options selected
                           using (differ.GetChanges(EntityManagerDifferOptions.Default, Allocator.TempJob))
                           {
                           }
                       })
                       .Definition("EntityManagerDiffer")
                       .WarmupCount(1)
                       .MeasurementCount(100)
                       .Run();
            }

        }
        
        /// <summary>
        /// Test case for an incremental update where at least one change happens.
        ///
        /// This is the most common use case where chunk diffing is incremental BUT lookups must be generated.
        /// </summary>
#if UNITY_2019_2_OR_NEWER
        [Test, Performance]
#else
        [PerformanceTest]
#endif
        [TestCase(1000)]
        [TestCase(10000)]
        [TestCase(100000)]
        public void PerformanceTest_EntityManagerDiffer_GetChanges_ForwardChangesOnly_SingleChange(int entityCount)
        {
            CreateEntitiesWithMockComponentData(SrcEntityManager, entityCount, typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestSharedComp));

            var entity = default(Entity);
            
            using (var entities = SrcEntityManager.GetAllEntities(Allocator.Temp))
            {
                entity = entities[0];
            }

            using (var differ = new EntityManagerDiffer(SrcWorld, Allocator.TempJob))
            {
                // Fast forward the shadow world
                using (differ.GetChanges(EntityManagerDifferOptions.FastForwardShadowWorld, Allocator.TempJob))
                {
                    
                }
                
                var startValue = 99;

                Measure.Method(() =>
                       {
                           SrcEntityManager.SetComponentData(entity, new EcsTestData(startValue++));

                           // Get changes with all options selected
                           using (differ.GetChanges(EntityManagerDifferOptions.IncludeForwardChangeSet, Allocator.TempJob))
                           {
                           }
                       })
                       .Definition("EntityManagerDiffer")
                       .WarmupCount(1)
                       .MeasurementCount(100)
                       .Run();
            }
        }
    }
}