using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Wall : MonoBehaviour
{
    public AudioClip Explosion;        //1 of 2 audio clips that play when the wall is blown up
    public AudioClip Explosion2;       //2 of 2 audio clips that play when the wall is blown up
    //Don't need alternate sprite
    public int hp = 1;                 //hit points for the wall

    private SpriteRenderer spriteRenderer;  //Store a component reference to the attached SpriteRenderer

    void Awake()
    {
        //Get a component reference to the SpriteRenderer.
        spriteRenderer = GetComponent<spriteRenderer>();
    }

    //DamageWall is called when the bomb blows up
    public void DamageWall (int loss)
    {
        //Calls a Random SFX thing. Not permenent. Need to make it spacial
        SoundManager.instance.RandomizeSfx(Explosion, Explosion2);

        //No damamged wall sprite

        //Subtract loss from hit point total
        hp -= loss;

        //If hit points are less than or equal to zero:
        if(hp <=0)
        {
            gameObject.SetActive(false);
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
