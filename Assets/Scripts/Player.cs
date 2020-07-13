using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(PlayerController))]
[RequireComponent(typeof(GunController))]
[RequireComponent(typeof(TurretController))]
public class Player : MonoBehaviour
{
    public float moveSpeed = 5f;
    public Rigidbody2D rb;
    public Animator animator;


    Vector2 movement;

    PlayerController controller;
    TurretController turret;

    // Update is called once per frame
    private void Start()
    {
        controller = GetComponent<PlayerController>();
        turret = GetComponent<TurretController>();
    }
    void Update()
    {
        //Input
        movement.x = Input.GetAxisRaw("Horizontal");
        movement.y = Input.GetAxisRaw("Vertical");

        animator.SetFloat("Horizontal", movement.x);
        animator.SetFloat("Vertical", movement.y);
        animator.SetFloat("Speed", movement.sqrMagnitude);
    }

    private void FixedUpdate()
    {
        //Movement

        rb.MovePosition(rb.position + movement * moveSpeed * Time.fixedDeltaTime);
    }

    void HoldingTurret(TurretController holdingTurret)
    {

    }
}
