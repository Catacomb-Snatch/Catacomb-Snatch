using UnityEngine;
using System;
using System.Collections.Generic;
using Random = UnityEngine.Random;

//THIS IS RANDOMLY MADE EACH TIME. REMEMBER TO GET RID OF THE RANDOM STUFF
public class NewBehaviourScript : MonoBehaviour
{
    [Serializable]
    public class Count
    {
        public int minimum;
        public int maximum;

        public Count(int min, int max)
        {
            minimum = min;
            maximum = max;
        }
    }

    /*How big the board is. Including walls, 
     Catacomb Snatch is 49, but we don't stuff spawning on the walls, so it's 47
    */
    public int columns = 47;
    public int rows = 47;

    //Random range for walls in a level (min of 5 walls, max = 9)
    public Count wallCount = new Count(5, 9);
    //Same as before
    public Count foodCount = new Count(1, 5);

    //Variables to hold prefabs that we will spawn
    public GameObject exit;

    /*Apparently these are 'raises' which hold multiple object, then chose which one to put in.
      I think it's for when you have multiple textures of a tile, like the walls in Catacomb Snatch
      It chooses a random texture out of all the different wall textures and puts it in
    */
    public GameObject[] floorTiles;
    public GameObject[] wallTiles;
    public GameObject[] foodTiles;
    public GameObject[] enemyTiles;
    public GameObject[] outerWallTiles;

    //boardHolder keeps the hierarchy clean by childing lots of the spawned objects to boardHolder
    private Transform boardHolder;

    //Checks to see where objects have been spawned. Should be useful for the Sarcophaguses
    private List<Vector3> gridPositions = new List<Vector3>();


    void InitializedList()
    {
        //Clears gridPositions
        gridPositions.Clear();

        /*Loops fill positions on the game board as a Vector3
        Creates all posible positions for walls, enemies, items, etc
        */
        for (int x = 1; x < columns; x++)
        {
            for (int y = 1; y < rows; y++)
            {
                gridPositions.Add(new Vector3(x, y, 0f));
            }
        }
    }

    //Setting up outerwall and floor
    void BaordSetup()
    {
        boardHolder = new GameObject("Board").transform;
        //These are negative 'cause they are outside of the playing area
        for (int x = -1; x < columns + 1; x++)
        {
            for (int y = -1; y < rows + 1; y++)
            {
                GameObject toInstaniate = floorTiles[Random.Range(0, floorTiles.Length)];
                //Checks for posistioning for outerwalls
                if (x == -1 || x == columns || y == -1 || y == rows)
                    toInstaniate = outerWallTiles[Random.Range(0, outerWallTiles.Length)];

                GameObject instance = Instantiate(toInstaniate, new Vector3(x, y, 0f), Quaternion.identity) as GameObject;

                instance.transform.SetParent(boardHolder);
            }
        }
    }

    Vector3 RandomPosition()
    {
        int randomIndex = Random.Range(0, gridPositions.Count);
        Vector3 randomPosition = gridPositions[randomIndex];
        //Removes the position where an object has been spawned
        gridPositions.RemoveAt(randomIndex);
        return randomPosition;
    }

    //Spawns tiles at pos
    void LayoutObjectAtRandom(GameObject[] tileArray, int minimum, int maximum)
    {
        int objectCount = Random.Range(minimum, maximum + 1);

        for (int i = 0; i < objectCount; i++)
        {
            Vector3 randomPosition = RandomPosition();
            GameObject tileChoice = tileArray[Random.Range(0, tileArray.Length)];
            Instantiate(tileChoice, randomPosition, Quaternion.identity);
        }
    }

    public void SetupScene(int level)
    {
        BoardSetup();
        InitializeList();
        LayoutObjectAtRandom(wallTiles, wallCount.minimum, wallCount.maximum);
        LayoutObjectAtRandom(foodTiles, foodCount.minimum, foodCount.maximum);
        int enemyCount = (int)Mathf.Log(level, 2f);
        LayoutObjectAtRandom(enemyTiles, enemyCount, enemyCount);
    }
}