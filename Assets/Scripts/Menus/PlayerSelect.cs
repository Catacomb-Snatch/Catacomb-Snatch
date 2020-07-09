using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerSelect : MonoBehaviour
{
    private Animator anim;
    void Start()
    {
        anim = gameObject.GetComponent<Animator>();
    }
    public void Selected()
    {
        if (anim != null)
        {
            anim.Play("Lard_Right_Gun");
        }
    }
}
