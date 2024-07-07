import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonGame 
{
    public static void main(String[] args) 
    {
        Pokemon charmander = new FirePokemon("Charmander", 100, 52, 43, 60, 50, 65, 5, true);
        Move ember = new Move("Ember", "Fire", 40, 100, "Special", "Burn");
        charmander.learnMove(ember);

        Trainer ash = new Trainer("Ash");
        ash.addPokemonToTeam(charmander);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", "Grass", 100, 49, 49, 65, 65, 45, 5, true);
        Trainer misty = new Trainer("Misty");
        Move Razor_Leaf = new Move("Razor Leaf","Grass",55,100,"Normal","Decrease_Health");
        bulbasaur.learnMove(Razor_Leaf);
        misty.addPokemonToTeam(bulbasaur);

        ash.battle(misty);
    }
}

class Pokemon 
{
    private String name;
    private String type;
    private int health;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private int level;
    private int experience;
    private List<Move> moves;
    private boolean isWild;

    public Pokemon(String name, String type, int health, int attack, int defense, int specialAttack, int specialDefense, int speed, int level, boolean isWild) 
    {
        this.name = name;
        this.type = type;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.level = level;
        this.experience = 0;
        this.moves = new ArrayList<>();
        this.isWild = isWild;
    }

    public void attack(Pokemon opponent, Move move) 
    {
        if (move.getAccuracy() >= new Random().nextInt(100)) 
        {
            int damage = (move.getPower() * this.attack / opponent.defense) / 2;
            opponent.takeDamage(damage);
            System.out.println(this.name + " used " + move.getName() + " and dealt " + damage + " damage to " + opponent.getName());
            move.applyEffect(opponent,move);
        } else {
            System.out.println(this.name + " used " + move.getName() + " but missed!");
        }
    }

    public void takeDamage(int damage) 
    {
        this.health -= damage;
        if (this.health <= 0) 
        {
            this.health = 0;
            System.out.println(this.name + " fainted!");
        } else {
            System.out.println(this.name + " has " + this.health + " HP remaining.");
        }
    }

    public void gainExperience(int exp) 
    {
        this.experience += exp;
        System.out.println(this.name + " gained " + exp + " experience points.");
        if (this.experience >= this.level * 100) 
        {
            levelUp();
        }
    }

    public void levelUp() 
    {
        this.level++;
        this.attack += 2;
        this.defense += 2;
        this.specialAttack += 2;
        this.specialDefense += 2;
        this.speed += 2;
        this.health += 10;
        this.experience = 0;
        System.out.println(this.name + " leveled up to level " + this.level + "!");
    }

    public void learnMove(Move move) 
    {
        if (this.moves.size() < 4) 
        {
            this.moves.add(move);
            System.out.println(this.name + " learned " + move.getName() + "!");
        } else {
            System.out.println(this.name + " already knows 4 moves. Cannot learn " + move.getName());
        }
    }

    public int getStat(String stat) 
    {
        switch (stat.toLowerCase()) 
        {
            case "health": return health;
            case "attack": return attack;
            case "defense": return defense;
            case "special attack": return specialAttack;
            case "special defense": return specialDefense;
            case "speed": return speed;
            case "level": return level;
            case "experience": return experience;
            default: throw new IllegalArgumentException("Invalid stat name");
        }
    }

    public String getName() 
    {
        return name;
    }

    public List<Move> getMoves() 
    {
        return moves;
    }
}

class Move 
{
    private String name;
    private String type;
    private int power;
    private int accuracy;
    private String category;
    private String effect;

    public Move(String name, String type, int power, int accuracy, String category, String effect) 
    {
        this.name = name;
        this.type = type;
        this.power = power;
        this.accuracy = accuracy;
        this.category = category;
        this.effect = effect;
    }

    public void applyEffect(Pokemon target,Move move) 
    {
        if (this.effect != null && !this.effect.isEmpty()) 
        {
            switch (this.effect.toLowerCase()) 
            {
                case "burn":
                    System.out.println(target.getName() + " is burned!");
                    target.takeDamage(10);  
                    break;
                case "Decrease_Health":
                    System.out.println(target.getName() + " dealt "+move.getPower()+" damage!");
                    target.takeDamage(move.getPower());  
                    break;
                default:
                    System.out.println("No effect applied.");
                    break;
            }
        }
    }

    public String getName() 
    {
        return name;
    }

    public String getType() 
    {
        return type;
    }

    public int getPower() 
    {
        return power;
    }

    public int getAccuracy() 
    {
        return accuracy;
    }

    public String getCategory() 
    {
        return category;
    }

    public String getEffect() 
    {
        return effect;
    }
}

class Trainer 
{
    private String name;
    private List<Pokemon> team;
    private List<Item> inventory;

    public Trainer(String name) 
    {
        this.name = name;
        this.team = new ArrayList<>();
        this.inventory = new ArrayList<>();
    }

    public void catchPokemon(Pokemon wildPokemon, Item pokeball) 
    {
        Random random = new Random();
        int catchRate = pokeball.getType().equals("Ultra Ball") ? 75 : 50;
        if (random.nextInt(100) < catchRate) 
        {
            addPokemonToTeam(wildPokemon);
            System.out.println(this.name + " caught " + wildPokemon.getName() + "!");
        } else {
            System.out.println(wildPokemon.getName() + " escaped!");
        }
    }

    public void useItem(Item item, Pokemon target) 
    {
        item.use(target);
        System.out.println(this.name + " used " + item.getName() + " on " + target.getName());
    }

    public void battle(Trainer opponent) 
    {
        System.out.println(this.name + " is battling " + opponent.name + "!");
        while (this.hasAlivePokemon() && opponent.hasAlivePokemon()) 
        {
            Pokemon myPokemon = this.getFirstAlivePokemon();
            Pokemon opponentPokemon = opponent.getFirstAlivePokemon();
            
            if (!myPokemon.getMoves().isEmpty() && !opponentPokemon.getMoves().isEmpty()) 
            {
                Move myMove = myPokemon.getMoves().get(0); 
                Move opponentMove = opponentPokemon.getMoves().get(0); 

                if (myPokemon.getStat("speed") >= opponentPokemon.getStat("speed")) 
                {
                    myPokemon.attack(opponentPokemon, myMove);
                    if (opponentPokemon.getStat("health") > 0) 
                    {
                        opponentPokemon.attack(myPokemon, opponentMove);
                    }
                } else {
                    opponentPokemon.attack(myPokemon, opponentMove);
                    if (myPokemon.getStat("health") > 0) {
                        myPokemon.attack(opponentPokemon, myMove);
                    }
                }
            } else {
                System.out.println("A Pokémon has no moves and cannot attack!");
                break;
            }
        }

        if (this.hasAlivePokemon()) 
        {
            System.out.println(this.name + " wins!");
        } else {
            System.out.println(opponent.name + " wins!");
        }
    }

    public void addPokemonToTeam(Pokemon pokemon) 
     {
        if (team.size() < 6) 
        {
            team.add(pokemon);
            System.out.println(this.name + " added " + pokemon.getName() + " to the team!");
        } else {
            System.out.println("Team is full!");
        }
    }

    public void addItemToInventory(Item item) 
    {
        inventory.add(item);
        System.out.println(this.name + " added " + item.getName() + " to the inventory!");
    }

    public boolean hasAlivePokemon() 
    {
        for (Pokemon p : team) {
            if (p.getStat("health") > 0) 
            {
                return true;
            }
        }
        return false;
    }

    public Pokemon getFirstAlivePokemon() 
    {
        for (Pokemon p : team) 
        {
            if (p.getStat("health") > 0) 
            {
                return p;
            }
        }
        return null;
    }
}

class Item 
{
    private String name;
    private String type;
    private String effect;

    public Item(String name, String type, String effect) 
    {
        this.name = name;
        this.type = type;
        this.effect = effect;
    }

    public void use(Pokemon target) 
    {
        switch (this.effect.toLowerCase()) 
        {
            case "heal":
                target.takeDamage(-50);  
                System.out.println(target.getName() + " is healed!");
                break;
            default:
                System.out.println("No effect applied.");
                break;
        }
    }

    public String getName() 
    {
        return name;
    }

    public String getType() 
    {
        return type;
    }

    public String getEffect() 
    {
        return effect;
    }
}

class FirePokemon extends Pokemon 
{
    public FirePokemon(String name, int health, int attack, int defense, int specialAttack, int specialDefense, int speed, int level, boolean isWild) 
    {
        super(name, "Fire", health, attack, defense, specialAttack, specialDefense, speed, level, isWild);
    }
}
