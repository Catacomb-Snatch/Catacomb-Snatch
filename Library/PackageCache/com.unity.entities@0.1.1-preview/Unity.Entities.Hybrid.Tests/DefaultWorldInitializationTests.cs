﻿using NUnit.Framework;
using UnityEngine.TestTools;

namespace Unity.Entities.Tests
{
    public class DefaultWorldInitializationTests
    {
        private World m_PreviousWorld;

        [SetUp]
        public void Setup()
        {
            m_PreviousWorld = World.Active;
        }

        [Test]
        public void Initialize_ShouldLogNothing()
        {
            DefaultWorldInitialization.Initialize("Test World", true);

            LogAssert.NoUnexpectedReceived();
        }

        [TearDown]
        public void TearDown()
        {
            World.Active.Dispose();
            World.Active = null;

            World.Active = m_PreviousWorld;
            ScriptBehaviourUpdateOrder.UpdatePlayerLoop(null);
        }
    }
}
