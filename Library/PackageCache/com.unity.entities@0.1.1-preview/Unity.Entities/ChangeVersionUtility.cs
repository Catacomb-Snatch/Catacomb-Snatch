﻿namespace Unity.Entities
{
    static public class ChangeVersionUtility
    {
        public static bool DidChange(uint changeVersion, uint requiredVersion)
        {
            // When a system runs for the first time, everything is considered changed.
            if (requiredVersion == 0)
                return true;
            // Supporting wrap around for version numbers, change must be bigger than last system run.
            // (Never detect change of something the system itself changed)
            return (int)(changeVersion - requiredVersion) > 0;
        }

        public static void IncrementGlobalSystemVersion(ref uint globalSystemVersion)
        {
            globalSystemVersion++;
            // Handle wrap around, 0 is reserved for systems that have never run..
            if (globalSystemVersion == 0)
                globalSystemVersion++;
        }

        // 0 is reserved for systems that have never run
        public const int InitialGlobalSystemVersion = 1;

    }
}
