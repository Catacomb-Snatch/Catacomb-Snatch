using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UnityEngine.Experimental.U2D.Animation
{
    internal class SpriteSkinManager
    {
        // Doing this to hide it from user adding it from Inspector
        [DefaultExecutionOrder(0)]
        [ExecuteInEditMode]
        internal class SpriteSkinManagerInternal : MonoBehaviour
        {
            void LateUpdate()
            {
                if (SpriteSkinComposite.instance.helperGameObject != gameObject)
                {
                    GameObject.DestroyImmediate(gameObject);
                    return;
                }
                SpriteSkinComposite.instance.LateUpdate();
            }
        }
    }
}
