﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace SuperTiled2Unity.Editor
{
    public class ChDir : IDisposable
    {
        private string m_RestoreDirectory;

        public ChDir(string path)
        {
            m_RestoreDirectory = Directory.GetCurrentDirectory();

            if (Directory.Exists(path))
            {
                Directory.SetCurrentDirectory(path);
            }
            else if (File.Exists(path))
            {
                Directory.SetCurrentDirectory(Path.GetDirectoryName(path));
            }
        }

        public void Dispose()
        {
            Directory.SetCurrentDirectory(m_RestoreDirectory);
        }
    }
}
