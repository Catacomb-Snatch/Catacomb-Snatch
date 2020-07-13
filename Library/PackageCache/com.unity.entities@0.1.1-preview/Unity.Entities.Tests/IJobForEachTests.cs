using System;
using NUnit.Framework;
using Unity.Collections;
using Unity.Jobs;

namespace Unity.Entities.Tests
{
    public class IJobForEachTests :ECSTestsFixture
    {
        const int TEST_VALUE = 42;

        struct Process1 : IJobForEach<EcsTestData>
        {
            public void Execute(ref EcsTestData dst)
            {
                dst.value = 7;
            }
        }

        struct Process2 : IJobForEach<EcsTestData, EcsTestData2>
        {
            public void Execute([ReadOnly]ref EcsTestData src, ref EcsTestData2 dst)
            {
                dst.value1 = src.value;
            }
        }

        struct Process3Entity  : IJobForEachWithEntity<EcsTestData, EcsTestData2, EcsTestData3>
        {
            public void Execute(Entity entity, int index, [ReadOnly]ref EcsTestData src, ref EcsTestData2 dst1, ref EcsTestData3 dst2)
            {
                dst1.value1 = dst2.value2 = src.value + index + entity.Index;
            }
        }

        [Test]
        public void JobProcessSimple()
        {
            var entity = m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2));
            m_Manager.SetComponentData(entity, new EcsTestData(42));

            new Process2().Run(EmptySystem);
            
            Assert.AreEqual(42, m_Manager.GetComponentData<EcsTestData2>(entity).value1);
        }
                
        [Test]
        public void JobProcessComponentGroupCorrect()
        {
            ComponentType[] expectedTypes = { ComponentType.ReadOnly<EcsTestData>(), ComponentType.ReadWrite<EcsTestData2>() };

            new Process2().Run(EmptySystem);
            var group = EmptySystem.GetEntityQuery(expectedTypes);
                        
            Assert.AreEqual(1, EmptySystem.EntityQueries.Length);
            Assert.IsTrue(EmptySystem.EntityQueries[0].CompareComponents(expectedTypes));
            Assert.AreEqual(group, EmptySystem.EntityQueries[0]);
        }
        
                        
        [Test]
        public void JobProcessComponentGroupCorrectNativeArrayOfComponentTypes()
        {
            ComponentType[] initialTypes = { typeof(EcsTestData), typeof(EcsTestData2) };

            var archetype = m_Manager.CreateArchetype(initialTypes);
            var entity = m_Manager.CreateEntity(archetype);
            new Process2().Run(EmptySystem);
            var componentTypes = m_Manager.GetComponentTypes(entity);
            
            Assert.IsTrue(componentTypes[0] == initialTypes[0]);
            Assert.IsTrue(componentTypes[1] == initialTypes[1]);

            componentTypes[0] = ComponentType.ReadOnly(componentTypes[0].TypeIndex);
            
            var group = EmptySystem.GetEntityQuery(componentTypes);
                        
            Assert.AreEqual(1, EmptySystem.EntityQueries.Length);
            Assert.IsTrue(EmptySystem.EntityQueries[0].CompareComponents(componentTypes));
            Assert.AreEqual(group, EmptySystem.EntityQueries[0]);
            
            componentTypes.Dispose();
        }

        [Test]
        public void JobProcessComponentWithEntityGroupCorrect()
        {
            ComponentType[] expectedTypes = { ComponentType.ReadOnly<EcsTestData>(), ComponentType.ReadWrite<EcsTestData2>(), ComponentType.ReadWrite<EcsTestData3>() };

            new Process3Entity().Run(EmptySystem);
            var group = EmptySystem.GetEntityQuery(expectedTypes);
                        
            Assert.AreEqual(1, EmptySystem.EntityQueries.Length);
            Assert.IsTrue(EmptySystem.EntityQueries[0].CompareComponents(expectedTypes));
            Assert.AreEqual(group, EmptySystem.EntityQueries[0]);
        }
        

        class ChainedProcessComponentDataWorks : JobComponentSystem
        {
            protected override JobHandle OnUpdate(JobHandle inputDeps)
            {
                inputDeps = new Process1().Schedule(this, inputDeps);
                inputDeps = new Process2().Schedule(this, inputDeps);
                return inputDeps;
            }
        }
        [Test]
        public void MultipleJobForEachCanChain()
        {
            var entity = m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2));
            var system = World.GetOrCreateSystem<ChainedProcessComponentDataWorks>();
            system.Update();
            Assert.AreEqual(7, m_Manager.GetComponentData<EcsTestData2>(entity).value1);
        }

        public struct ArrayJob : IJob
        {
            public NativeArray<int> arr;

            public void Execute()
            {
                for (int i = 0; i < arr.Length; ++i)
                    arr[i] = 1;
            }
        }

        public struct ProcessArrayJob : IJobForEach<EcsTestData>
        {
            public NativeArray<int> arr;

            public void Execute(ref EcsTestData dst)
            {
                arr[0] = arr[0] + 1;
                dst.value = arr.Length;
            }
        }

        [Test]
        public void ForEachJobHandleDependency()
        {
            // A useful general test (that a dependency works between a Job and a JobForEach)
            // but specifically testing that (when this is written) DOTS-RT has a single threaded
            // JobForEach and a multi-threaded Job.
            const int N = 10000;

            var entity = m_Manager.CreateEntity(typeof(EcsTestData));
            m_Manager.SetComponentData(entity, new EcsTestData(-1));
            NativeArray<int> arr = new NativeArray<int>(N, Allocator.TempJob);

            ArrayJob arrayJob = new ArrayJob() {arr = arr};
            ProcessArrayJob processArrayJob = new ProcessArrayJob() {arr = arr};

            JobHandle handle = arrayJob.Schedule();    // long running
            JobHandle handle2 = processArrayJob.Schedule(EmptySystem, handle); // super fast

            handle2.Complete();
            Assert.AreEqual(N, m_Manager.GetComponentData<EcsTestData>(entity).value);
            Assert.AreEqual(2, arr[0]);
            Assert.AreEqual(1, arr[1]);

            arr.Dispose();
        }

#if ENABLE_UNITY_COLLECTIONS_CHECKS
        [Test]
        [StandaloneFixme]
        public void JobWithMissingDependency()
        {
            Assert.IsTrue(Unity.Jobs.LowLevel.Unsafe.JobsUtility.JobDebuggerEnabled, "JobDebugger must be enabled for these tests");

            m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2));

            var job = new Process2().Schedule(EmptySystem);
            Assert.Throws<InvalidOperationException>(() => { new Process2().Schedule(EmptySystem); });
            
            job.Complete();
        }
#endif
        
        [ExcludeComponent(typeof(EcsTestData3))]
        [RequireComponentTag(typeof(EcsTestData4))]
        struct ProcessTagged1 : IJobForEach<EcsTestData, EcsTestData2>
        {
            public void Execute(ref EcsTestData src, ref EcsTestData2 dst)
            {
                dst.value1 = dst.value0 = src.value;
            }
        }
        
        void TestExcludeRequire1(bool didProcess, Entity entity)
        {
            m_Manager.SetComponentData(entity, new EcsTestData(TEST_VALUE));

            new ProcessTagged1().Schedule(EmptySystem).Complete();

            Assert.AreEqual(didProcess ? TEST_VALUE : 0, m_Manager.GetComponentData<EcsTestData2>(entity).value0);
        }

        [ExcludeComponent(typeof(EcsTestData4), typeof(EcsTestData3))]
        struct ProcessTagged2 : IJobForEach<EcsTestData, EcsTestData2>
        {
            public void Execute(ref EcsTestData src, ref EcsTestData2 dst)
            {
                dst.value1 = dst.value0 = src.value;
            }
        }

        void TestExcludeRequire2(bool didProcess, Entity entity)
        {
            m_Manager.SetComponentData(entity, new EcsTestData(TEST_VALUE));

            new ProcessTagged2().Schedule(EmptySystem).Complete();

            Assert.AreEqual(didProcess ? TEST_VALUE : 0, m_Manager.GetComponentData<EcsTestData2>(entity).value0);
        }

        [RequireComponentTag(typeof(EcsTestData4), typeof(EcsTestData3))]
        struct ProcessTagged3 : IJobForEach<EcsTestData, EcsTestData2>
        {
            public void Execute(ref EcsTestData src, ref EcsTestData2 dst)
            {
                dst.value1 = dst.value0 = src.value;
            }
        }

        void TestExcludeRequire3(bool didProcess, Entity entity)
        {
            m_Manager.SetComponentData(entity, new EcsTestData(TEST_VALUE));

            new ProcessTagged3().Schedule(EmptySystem).Complete();

            Assert.AreEqual(didProcess ? TEST_VALUE : 0, m_Manager.GetComponentData<EcsTestData2>(entity).value0);
        }

        [Test]
        public void JobProcessAdditionalRequirements()
        {
            /*
             * [ExcludeComponent(typeof(EcsTestData3))]
             * [RequireComponentTag(typeof(EcsTestData4))]
             */
            TestExcludeRequire1(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3)));

            TestExcludeRequire1(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2)));

            TestExcludeRequire1(true,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData4)));

            /*
             * [ExcludeComponent(typeof(EcsTestData4), typeof(EcsTestData3))]
             */
            TestExcludeRequire2(true,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2)));
            TestExcludeRequire2(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3)));
            TestExcludeRequire2(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData4)));
            TestExcludeRequire2(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3), typeof(EcsTestData4)));

            /*
             * [RequireComponentTag(typeof(EcsTestData4), typeof(EcsTestData3))]
             */
            TestExcludeRequire3(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2)));
            TestExcludeRequire3(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3)));
            TestExcludeRequire3(false,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData4)));
            TestExcludeRequire3(true,
                m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3), typeof(EcsTestData4)));
        }

        struct ProcessFilteredData : IJobForEach<EcsTestData>
        {
            public void Execute(ref EcsTestData c0)
            {
                c0 = new EcsTestData {value = 10};
            }
        }

        [Test]
        public void JobProcessWithFilteredEntityQuery()
        {
            var archetype = m_Manager.CreateArchetype(typeof(EcsTestData), typeof(EcsTestSharedComp));

            var entityInGroupA = m_Manager.CreateEntity(archetype);
            var entityInGroupB = m_Manager.CreateEntity(archetype);
            
            m_Manager.SetComponentData<EcsTestData>(entityInGroupA, new EcsTestData{value = 5});
            m_Manager.SetComponentData<EcsTestData>(entityInGroupB, new EcsTestData{value = 5});
            m_Manager.SetSharedComponentData<EcsTestSharedComp>(entityInGroupA, new EcsTestSharedComp { value = 1} );
            m_Manager.SetSharedComponentData<EcsTestSharedComp>(entityInGroupB, new EcsTestSharedComp { value = 2} );
           
            var group = EmptySystem.GetEntityQuery(typeof(EcsTestData), typeof(EcsTestSharedComp));
            group.SetFilter(new EcsTestSharedComp { value = 1});
            
            var processJob = new ProcessFilteredData();
            processJob.Schedule(group).Complete();
            
            Assert.AreEqual(10, m_Manager.GetComponentData<EcsTestData>(entityInGroupA).value);
            Assert.AreEqual(5,  m_Manager.GetComponentData<EcsTestData>(entityInGroupB).value);
        }

#if !UNITY_DOTSPLAYER
        [Test]
        public void JobProcessWithMismatchedComponentGroupThrowsException()
        {
            var query = EmptySystem.GetEntityQuery(typeof(EcsTestData));
            Assert.Throws<InvalidOperationException>(() => { new Process2().Schedule(query); });
        }

        [Test]
        public void JobCalculateEntityCount()
        {
            m_Manager.CreateEntity(typeof(EcsTestData));
            m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2));
            m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2));
            m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3));
            m_Manager.CreateEntity(typeof(EcsTestData), typeof(EcsTestData2), typeof(EcsTestData3));

            var job = new Process1();
            Assert.AreEqual(5, job.CalculateEntityCount(EmptySystem));
            job.Schedule(EmptySystem).Complete();
            
            var job2 = new Process2();
            Assert.AreEqual(4, job2.CalculateEntityCount(EmptySystem));
            job2.Schedule(EmptySystem).Complete();
        }

        [Test]
        public void JobExcludedComponentExplicitQuery()
        {
            var group = EmptySystem.GetEntityQuery(ComponentType.Exclude<EcsTestData>());

            var handle = new JobHandle();
            Assert.Throws<InvalidOperationException>(() => handle = new Process1().Schedule(group));
            handle.Complete();
        }
#endif

        [Test]
        [Ignore("TODO")]
        public void TestCoverageFor_ComponentSystemBase_InjectNestedIJobForEachJobs()
        {
        }
        
        [Test]
        [Ignore("TODO")]
        public void DuplicateComponentTypeParametersThrows()
        {
        }
    }
}
