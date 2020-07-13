using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Pathfinding;


public class Enemy : MonoBehaviour
{
    Vector2 movement;
    public Animator animator;

    public AIPath aiPath;

    void Start()
    {



        

    }

    void Update()
    {
        print("Pos X:" + aiPath.desiredVelocity.y);
        print("Pos Y:" + aiPath.desiredVelocity.x);

        if (aiPath.desiredVelocity.y >= 0.01f)
        {
            animator.SetFloat("Horizontal", 1);
        } else if (aiPath.desiredVelocity.y <= -0.01)
        {
            animator.SetFloat("Horizontal", -1);
        }

        if(aiPath.desiredVelocity.x >= 0.01f)
        {
            animator.SetFloat("Vertical", 1);
        } else if (aiPath.desiredVelocity.x <= -0.01)
        {
            animator.SetFloat("Vertical", -1);
        }

    }
}
