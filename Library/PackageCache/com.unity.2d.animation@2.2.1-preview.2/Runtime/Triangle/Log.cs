// -----------------------------------------------------------------------
// <copyright file="Log.cs" company="">
// Triangle.NET code by Christian Woltering, http://triangle.codeplex.com/
// </copyright>
// -----------------------------------------------------------------------

namespace UnityEngine.Experimental.U2D.Animation.TriangleNet
{
    using System.Collections.Generic;
    using Animation.TriangleNet.Logging;

    /// <summary>
    /// A simple logger, which logs messages to a List.
    /// </summary>
    /// <remarks>Using singleton pattern as proposed by Jon Skeet.
    /// http://csharpindepth.com/Articles/General/Singleton.aspx
    /// </remarks>
    internal sealed class Log : ILog<LogItem>
    {
        /// <summary>
        /// Log detailed information.
        /// </summary>
        internal static bool Verbose { get; set; }

        private List<LogItem> log = new List<LogItem>();

        private LogLevel level = LogLevel.Info;

        #region Singleton pattern

        private static readonly Log instance = new Log();

        // Explicit static constructor to tell C# compiler
        // not to mark type as beforefieldinit
        static Log() {}

        private Log() {}

        internal static ILog<LogItem> Instance
        {
            get
            {
                return instance;
            }
        }

        #endregion

        public void Add(LogItem item)
        {
            log.Add(item);
        }

        public void Clear()
        {
            log.Clear();
        }

        public void Info(string message)
        {
            log.Add(new LogItem(LogLevel.Info, message));
        }

        public void Warning(string message, string location)
        {
            log.Add(new LogItem(LogLevel.Warning, message, location));
        }

        public void Error(string message, string location)
        {
            log.Add(new LogItem(LogLevel.Error, message, location));
        }

        public IList<LogItem> Data
        {
            get { return log; }
        }

        public LogLevel Level
        {
            get { return level; }
        }
    }
}
