using NUnit.Framework;
using System;

namespace Unity.Properties.Tests
{
    [TestFixture]
    class PropertyContainerTransferTests
    {
        [SetUp]
        public void SetUp()
        {
            TestData.InitializePropertyBags();
        }

        [Test]
        public void PropertyContainer_Transfer_Primitive()
        {
            var src = new TestPrimitiveContainer
            {
                Int32Value = 10
            };

            var dst = new TestPrimitiveContainer
            {
                Int32Value = 20
            };

            PropertyContainer.Transfer(ref dst, ref src);

            Assert.AreEqual(10, dst.Int32Value);
        }

        [Test]
        public void ShouldGiveUnderstandableErrorMessageWhenPassingNullDestination()
        {
            var src = new CustomDataFoo();
            CustomDataFoo dst = null;

            var ex = Assert.Throws<ArgumentNullException>(() => PropertyContainer.Transfer(dst, src));
            Assert.That(ex.ParamName, Is.EqualTo("destination"));
            Assert.That(ex.Message, Is.EqualTo("Value cannot be null." + Environment.NewLine +
                                               "Parameter name: destination"));
        }

        [Test]
        public void ShouldGiveUnderstandableErrorMessageWhenPassingNullDestinationByRef()
        {
            var src = new CustomDataFoo();
            CustomDataFoo dst = null;

            var ex = Assert.Throws<ArgumentNullException>(() => PropertyContainer.Transfer(ref dst, ref src));
            Assert.That(ex.ParamName, Is.EqualTo("destination"));
            Assert.That(ex.Message, Is.EqualTo("Value cannot be null." + Environment.NewLine +
                                               "Parameter name: destination"));
        }
    }
}
