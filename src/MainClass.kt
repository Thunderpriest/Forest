import java.security.SecureRandom
import java.util.*
import kotlin.collections.ArrayList

fun main(args : Array<String>)
{
    var forest : Forest = Forest()
    forest.start()
}

abstract class Animal(location: Location)
{
    val random : Random = SecureRandom()
    val gender : Boolean = random.nextBoolean() // true = male
    val maxHp : Int = 10
    var hp : Int = maxHp
    var location : Location = location

    abstract fun eat(dead : ArrayList<Animal>)
    abstract fun breed(born : ArrayList<Animal>)

    open fun move()
    {
        val oldlocation : Location = location
        var tree : Tree = location.tree
        var newLocation : Location

        if (random.nextBoolean())
        {
            tree = location.tree.nTrees[random.nextInt(location.tree.nTrees.size)]
        }

        when(random.nextInt(2))
        {
            0 -> newLocation = tree.crown
            1 -> newLocation = tree.trunk
            else -> newLocation = tree.roots
        }

        if (newLocation is Trunk && newLocation.hollows.isNotEmpty() && random.nextBoolean())
            newLocation = newLocation.hollows[random.nextInt(newLocation.hollows.size)]
        else if (newLocation is Roots && newLocation.holes.isNotEmpty() && random.nextBoolean())
            newLocation = newLocation.holes[random.nextInt(newLocation.holes.size)]

        oldlocation.animals.remove(this)
        newLocation.animals.add(this)
        location = newLocation
    }
}

class Wolf(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        val random : Random = SecureRandom()
        val animal : Animal = location.animals[random.nextInt(location.animals.size)]
        if (this != animal)
        {
            dead.add(animal)
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Wolf(location))
    }

    override fun move()
    {
        val oldlocation : Location = location
        var tree : Tree = location.tree
        var newLocation : Location

        if (random.nextBoolean())
        {
            tree = location.tree.nTrees[random.nextInt(location.tree.nTrees.size)]
        }

        newLocation = tree.roots

        oldlocation.animals.remove(this)
        newLocation.animals.add(this)
        location = newLocation
    }
}

class Vulture(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        val random : Random = SecureRandom()
        val animal : Animal = location.animals[random.nextInt(location.animals.size)]
        if (this != animal && animal !is Badger)
        {
            dead.add(animal)
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Vulture(location))
    }

    override fun move()
    {
        val oldlocation : Location = location
        var tree : Tree = location.tree
        var newLocation : Location

        if (random.nextBoolean())
        {
            tree = location.tree.nTrees[random.nextInt(location.tree.nTrees.size)]
        }

        newLocation = tree.crown

        oldlocation.animals.remove(this)
        newLocation.animals.add(this)
        location = newLocation
    }
}

class Squirrel(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        for (i in 0..location.resources.size-1)
        {
            val resource = location.resources[i]
            if (location is Crown && (resource is Nut || resource is Cone))
            {
                hp = maxHp
                location.resources.remove(resource)
                return
            }
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Squirrel(location))
    }
}

class Chipmunk(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        for (i in 0..location.resources.size-1)
        {
            val resource = location.resources[i]
            if (location is Roots && (resource is Nut || resource is Cone))
            {
                hp = maxHp
                location.resources.remove(resource)
                return
            }
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Chipmunk(location))
    }
}

class FSquirrel(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        for (i in 0..location.resources.size-1)
        {
            val resource = location.resources[i]
            if (resource is MapleLeaf)
            {
                hp = maxHp
                location.resources.remove(resource)
                return
            }
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(FSquirrel(location))
    }
}

class Woodpecker(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        for (i in 0..location.resources.size-1)
        {
            val resource = location.resources[i]
            if (resource is Worm)
            {
                hp = maxHp
                location.resources.remove(resource)
                return
            }
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Woodpecker(location))
    }
}

class Badger(location: Location) : Animal(location)
{
    override fun eat(dead: ArrayList<Animal>)
    {
        for (i in 0..location.resources.size-1)
        {
            val resource = location.resources[i]
            if (resource is RootCrop)
            {
                hp = maxHp
                location.resources.remove(resource)
                return
            }
        }
    }

    override fun breed(born: ArrayList<Animal>)
    {
        born.add(Badger(location))
    }
}

open class Resource
{
    override fun toString(): String
    {
        return super.toString()
    }
}

class Nut : Resource()
{
    override fun toString(): String
    {
        return "Nut"
    }
}

class Cone : Resource()
{
    override fun toString(): String
    {
        return "Cone"
    }
}

class MapleLeaf : Resource()
{
    override fun toString(): String
    {
        return "Maple Leaf"
    }
}

class Worm : Resource()
{
    override fun toString(): String
    {
        return "Worm"
    }
}

class RootCrop : Resource()
{
    override fun toString(): String
    {
        return "Root Crop"
    }
}

enum class TreeType
{
    FIR,
    PINE,
    OAK,
    BIRCH,
    MAPLE,
    WALNUT
}

open class Location(tree : Tree)
{
    var animals : ArrayList<Animal> = ArrayList<Animal>()
    var resources : ArrayList<Resource> = ArrayList<Resource>()

    val tree : Tree = tree

    open fun spawnResources()
    {
        // nothing happens
    }

    open fun spawnAnimals()
    {
        // nothing happens
    }
}

class Hollow(tree: Tree) : Location(tree)
{
    override fun spawnAnimals()
    {
        val random : Random = SecureRandom()
        val rnd : Boolean = random.nextBoolean()

        if (rnd)
        {
            val rand = random.nextInt(2)
            when (rand)
            {
                0 -> animals.add(Squirrel(this))
                1 -> animals.add(FSquirrel(this))
                else -> animals.add(Woodpecker(this))
            }
        }
    }
}

class Hole(tree: Tree) : Location(tree)
{
    override fun spawnAnimals()
    {
        val random : Random = SecureRandom()
        val rnd : Boolean = random.nextBoolean()

        if (rnd)
        {
            val rand = random.nextBoolean()
            if (rand)
                animals.add(Chipmunk(this))
            else
                animals.add(Badger(this))
        }
    }
}

class Crown(tree: Tree) : Location(tree)
{
    override fun spawnResources()
    {
        val random : Random = SecureRandom()
        val rnd = random.nextInt(20)

        if (tree.treeType == TreeType.WALNUT)
        {
            for (i in 0..rnd)
                resources.add(Nut())
        }
        else if (tree.treeType == TreeType.FIR || tree.treeType == TreeType.PINE)
        {
            for (i in 0..rnd)
                resources.add(Cone())
        }
        else if (tree.treeType == TreeType.MAPLE)
        {
            for (i in 0..10*rnd)
                resources.add(MapleLeaf())
        }
    }

    override fun spawnAnimals()
    {
        val random : Random = SecureRandom()
        val rnd : Double = random.nextDouble()

        if (rnd < 0.2)
        {
            val rand = random.nextBoolean()
            animals.add(Vulture(this))
        }
    }
}

class Trunk(tree: Tree) : Location(tree)
{
    val random : Random = SecureRandom()
    val hollows : Array<Hollow> = Array<Hollow>(random.nextInt(5), { i -> Hollow(tree) })

    override fun spawnResources()
    {
        val random: Random = SecureRandom()
        val rnd = random.nextInt(20)

        for (i in 0..rnd)
            resources.add(Worm())
    }
}

class Roots(tree: Tree) : Location(tree)
{
    val random : Random = SecureRandom()
    val holes : Array<Hole> = Array<Hole>(random.nextInt(5), { i -> Hole(tree) })

    override fun spawnResources()
    {
        val random : Random = SecureRandom()
        val rnd = random.nextInt(20)

        if (tree.treeType == TreeType.WALNUT)
        {
            for (i in 0..rnd)
                resources.add(Nut())
        }
        else if (tree.treeType == TreeType.FIR || tree.treeType == TreeType.PINE)
        {
            for (i in 0..rnd)
                resources.add(Cone())
        }

        if (random.nextBoolean())
        {
            val rand = random.nextInt(10)
            for (i in 0..rand)
                resources.add(RootCrop())
        }
    }

    override fun spawnAnimals()
    {
        val random : Random = SecureRandom()
        val rnd : Double = random.nextDouble()

        if (rnd < 0.2)
        {
            val rand = random.nextBoolean()
            animals.add(Wolf(this))
        }
    }
}

class Tree(treeType: TreeType, forest: Forest)
{
    var crown : Crown = Crown(this)
    var trunk : Trunk = Trunk(this)
    var roots : Roots = Roots(this)

    val treeType : TreeType = treeType
    val forest : Forest = forest

    val nTrees : ArrayList<Tree> = ArrayList<Tree>()
}

fun randomTree(forest: Forest) : Tree
{
    val random : Random = SecureRandom()
    val rnd = random.nextInt(6)

    when (rnd)
    {
        0 -> return Tree(TreeType.FIR, forest)
        1 -> return Tree(TreeType.PINE, forest)
        2 -> return Tree(TreeType.BIRCH, forest)
        3 -> return Tree(TreeType.MAPLE, forest)
        4 -> return Tree(TreeType.OAK, forest)
        else -> return Tree(TreeType.WALNUT, forest)
    }
}

class Forest
{
    val trees : Array<Tree> = Array<Tree>(20, { i -> randomTree(this)})
    var animals : ArrayList<Animal> = ArrayList<Animal>()
    var iteration : Int = 0
    var population : Int = 0

    fun bindTrees()
    {
        val random : Random = SecureRandom()

        for (i in 0..90)
        {
            val a = random.nextInt(trees.size)
            val b = random.nextInt(trees.size)

            if (a != b)
                trees[a].nTrees.add(trees[b])
                trees[b].nTrees.add(trees[a])
        }
    }

    fun initSpawn()
    {
        for (tree in trees)
        {
            for (hollow in tree.trunk.hollows)
            {
                hollow.spawnAnimals()
                animals.addAll(hollow.animals)
            }
            for (hole in tree.roots.holes)
            {
                hole.spawnAnimals()
                animals.addAll(hole.animals)
            }
        }
    }

    fun iter()
    {
        var dead : ArrayList<Animal> = ArrayList<Animal>()
        var born : ArrayList<Animal> = ArrayList<Animal>()

        if (iteration % 10 == 0)
        {
            for (tree in trees)
            {
                tree.crown.spawnResources()
                tree.trunk.spawnResources()
                tree.roots.spawnResources()
            }
        }

        for (animal in animals)
        {
            animal.hp -= 1
            animal.move()
            animal.eat(dead)

            if (animal.hp <= 0)
            {
                dead.add(animal)
                animal.location.animals.remove(animal)
            }

            for (anim in animal.location.animals)
            {
                if (anim::class == animal::class && anim.gender != animal.gender && SecureRandom().nextDouble() < 0.12)
                {
                    anim.breed(born)
                }
            }
        }

        for (animal in dead)
        {
            animals.remove(animal)
        }

        for (animal in born)
        {
            animals.add(animal)
            animal.location.animals.add(animal)
        }

        population = animals.size
        iteration++
    }

    fun start()
    {
        bindTrees()
        initSpawn()
        population = animals.size
        while (population > 0 && population < 1000)
        {
            iter()
            println(population)
        }
        println("The forest survived for " + iteration + " iterations")
    }
}