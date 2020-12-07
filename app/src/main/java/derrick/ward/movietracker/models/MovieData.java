package derrick.ward.movietracker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieData {

    private HashMap<String, Movie> moviesList;

    public HashMap<String, Movie> getMoviesList() {
        return moviesList;
    }

    public int getSize(){
        return moviesList.size();
    }

    public Movie getItem(int i){
        if (i >=0 && i < moviesList.size()){
            return (Movie) moviesList.get(i);
        } else return null;
    }

    public MovieData(){
        String description;
        String length;
        String year;
        double rating;
        String director;
        String stars;
        String url;
        String imageName;
        String imageStorageDomain = "gs://movietracker-3e36a.appspot.com/movieArt";
        moviesList = new HashMap<String, Movie>();
        //#1-10
        year = "2009";
        length = "162 min";
        rating = 7.9;
        director = "Cameron" ;
        stars = "Sam Worthington, Zoe Saldana, Sigourney Weaver";
        imageName = "avatar.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.";
        createAndAddMovie("Avatar", imageName, description, year, length, rating, director, stars, url);

        year = "1997";
        length = "194 min";
        rating = 7.7;
        director = "James Cameron" ;
        stars = "Leonardo DiCaprio, Kate Winslet, Billy Zane";
        imageName = "titanic.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Seventeen-year-old aristocrat, expecting to be married to a rich claimant by her mother, falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.";
        createAndAddMovie("Titanic", imageName, description, year, length, rating, director, stars, url);

        year = "2012";
        length = "143 min";
        rating = 8.2;
        director = "Joss Whedon" ;
        stars = "Robert Downey Jr., Chris Evans, Scarlett Johansson";
        imageName = "avengers.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Nick Fury of S.H.I.E.L.D. assembles a team of superheroes to save the Movie from Loki and his army.";
        createAndAddMovie("The Avengers", imageName, description, year, length, rating, director, stars, url);

        year = "2008";
        length = "152 min";
        rating = 9.0;
        director = "Christopher Nolan" ;
        stars = "Christian Bale, Heath Ledger, Aaron Eckhart";
        imageName = "dark_knight.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "When Batman, Gordon and Harvey Dent launch an assault on the mob, they let the clown out of the box, the Joker, bent on turning Gotham on itself and bringing any heroes down to his level.";
        createAndAddMovie("The Dark Knight", imageName, description, year, length, rating, director, stars, url);

        year = "1999";
        length = "136 min";
        rating = 6.6;
        director = "George Lucas" ;
        stars = "Ewan McGregor, Liam Neeson, Natalie Portman";
        imageName = "star_wars1.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Two Jedi Knights escape a hostile blockade to find allies and come across a young boy who may bring balance to the Force, but the long dormant Sith resurface to reclaim their old glory.";
        createAndAddMovie("Star Wars I", imageName, description, year, length, rating, director, stars, url);

        year = "1977";
        length = "121 min";
        rating = 8.7;
        director = "George Lucas" ;
        stars = "Mark Hamill, Harrison Ford, Carrie Fisher";
        imageName = "star_wards4.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a wookiee and two droids to save the universe from the Empire's world-destroying battle-station, while also attempting to rescue Princess Leia from the evil Darth Vader.";
        createAndAddMovie("Star Wars IV ", imageName, description, year, length, rating, director, stars, url);

        year = "2012";
        length = "165 min";
        rating = 8.6;
        director = "Christopher Nolan" ;
        stars = "Christian Bale, Tom Hardy, Anne Hathaway";
        imageName = "dark_knight_rises.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Eight years after the Joker's reign of anarchy, the Dark Knight must return to defend Gotham City against the enigmatic jewel thief Catwoman and the ruthless mercenary Bane as the city teeters on the brink of complete annihilation.";
        createAndAddMovie("The Dark Knight Rises", imageName, description, year, length, rating, director, stars, url);

        year = "2004";
        length = "93 min";
        rating = 7.3;
        director = "Andrew Adamson, Kelly Asbury" ;
        stars = "Mike Myers, Eddie Murphy, Cameron Diaz";
        imageName = "shrek2.jpg";
        url = imageStorageDomain+"/"+imageName;;
        description = "Princess Fiona's parents invite her and Shrek to dinner to celebrate her marriage. If only they knew the newlyweds were both ogres.";
        createAndAddMovie("Shrek 2", imageName, description, year, length, rating, director, stars, url);

        year = "1982";
        length = "115 min";
        rating = 7.9;
        director = "Steven Spielberg" ;
        stars = "Henry Thomas, Drew Barrymore, Peter Coyote";
        imageName = "et.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "A troubled child summons the courage to help a friendly alien escape Earth and return to his home-world.";
        createAndAddMovie("ET the Extra Terrestrial", imageName, description, year, length, rating, director, stars, url);

        year = "2013";
        length = "146 min";
        rating = 7.8;
        director = "Francis Lawrence" ;
        stars = "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth";
        imageName = "hunger_games.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Katniss Everdeen and Peeta Mellark become targets of the Capitol after their victory in the 74th Hunger Games sparks a rebellion in the Districts of Panem.";
        createAndAddMovie("The Hunger Games: Catching Fire", imageName, description, year, length, rating, director, stars, url);

        //#11-20
        year = "2006";
        length = "151 min";
        rating = 7.3;
        director = "Gore Verbinski" ;
        stars = "Johnny Depp, Orlando Bloom, Keira Knightley";
        imageName = "pirates.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Jack Sparrow races to recover the heart of Davy Jones to avoid enslaving his soul to Jones' service, as other friends and foes seek the heart for their own agenda as well.";
        createAndAddMovie("Pirates of the Caribbean: Dead Man's Chest", imageName, description, year, length, rating, director, stars, url);

        year = "1994";
        length = "89 min";
        rating = 8.5;
        director = "Roger Allers, Rob Minkoff" ;
        stars = "Matthew Broderick, Jeremy Irons, James Earl Jones";
        imageName = "lion.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Lion cub and future king Simba searches for his identity. His eagerness to please others and penchant for testing his boundaries sometimes gets him into trouble.";
        createAndAddMovie("The Lion King", imageName, description, year, length, rating, director, stars, url);

        year = "2010";
        length = "103 min";
        rating = 8.4;
        director = "Lee Unkrich" ;
        stars = "Tom Hanks, Tim Allen, Joan Cusack";
        imageName = "toy3.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "The toys are mistakenly delivered to a day-care center instead of the attic right before Andy leaves for college, and it's up to Woody to convince the other toys that they weren't abandoned and to return home.";
        createAndAddMovie("Toy Story 3", imageName, description, year, length, rating, director, stars, url);

        year = "2013";
        length = "130 min";
        rating = 7.4;
        director = "Shane Black" ;
        stars = "Robert Downey Jr., Guy Pearce, Gwyneth Paltrow";
        imageName = "ironman3.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "When Tony Stark's world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.";
        createAndAddMovie("Iron Man 3", imageName, description, year, length, rating, director, stars, url);

        year = "2012";
        length = "142 min";
        rating = 7.3;
        director = "Gary Ross" ;
        stars = "Jennifer Lawrence, Josh Hutcherson, Liam Hemsworth";
        imageName = "hunger_games1.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Katniss Everdeen voluntarily takes her younger sister's place in the Hunger Games, a televised fight to the death in which two teenagers from each of the twelve Districts of Panem are chosen at random to compete.";
        createAndAddMovie("The Hunger Games", imageName, description, year, length, rating, director, stars, url);

        year = "2002";
        length = "121 min";
        rating = 7.3;
        director = "Sam Raimi" ;
        stars = "Tobey Maguire, Kirsten Dunst, Willem Dafoe";
        imageName = "spiderman.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "When bitten by a genetically modified spider, a nerdy, shy, and awkward high school student gains spider-like abilities that he eventually must use to fight evil as a superhero after tragedy befalls his family.";
        createAndAddMovie("Spider-Man", imageName, description, year, length, rating, director, stars, url);

        year = "1993";
        length = "127 min";
        rating = 8.0;
        director = "Steven Spielberg" ;
        stars = "Sam Neill, Laura Dern, Jeff Goldblum";
        imageName = "jurassicpark.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "During a preview tour, a theme park suffers a major power breakdown that allows its cloned dinosaur exhibits to run amok.";
        createAndAddMovie("Jurassic Park", imageName, description, year, length, rating, director, stars, url);

        year = "2009";
        length = "150 min";
        rating = 6.0;
        director = "Michael Bay" ;
        stars = "Shia LaBeouf, Megan Fox, Josh Duhamel";
        imageName = "transformers.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Sam Witwicky leaves the Autobots behind for a normal life. But when his mind is filled with cryptic symbols, the Decepticons target him and he is dragged back into the Transformers' war.";
        createAndAddMovie("Transformers: Revenge of the Fallen", imageName, description, year, length, rating, director, stars, url);

        year = "2013";
        length = "102 min";
        rating = 7.9;
        director = "Chris Buck, Jennifer Lee" ;
        stars = "Kristen Bell, Idina Menzel, Jonathan Groff";
        imageName = "frozen.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Fearless optimist Anna teams up with Kristoff in an epic journey, encountering Everest-like conditions, and a hilarious snowman named Olaf in a race to find Anna's sister Elsa, whose icy powers have trapped the kingdom in eternal winter.";
        createAndAddMovie("Frozen", imageName, description, year, length, rating, director, stars, url);

        year = "2011";
        length = "130 min";
        rating = 8.1;
        director = "David Yates" ;
        stars = "Daniel Radcliffe, Emma Watson, Rupert Grint";
        imageName = "harry2.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Harry, Ron and Hermione search for Voldemort's remaining Horcruxes in their effort to destroy the Dark Lord as the final battle rages on at Hogwarts.";
        createAndAddMovie("Harry Potter and the Deathly Hallows: Part 2", imageName, description, year, length, rating, director, stars, url);

        //#21-30
        year = "2003";
        length = "100 min";
        rating = 8.2;
        director = "Andrew Stanton, Lee Unkrich" ;
        stars = "Albert Brooks, Ellen DeGeneres, Alexander Gould ";
        imageName = "nemo.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "After his son is captured in the Great Barrier Reef and taken to Sydney, a timid clownfish sets out on a journey to bring him home.";
        createAndAddMovie("Finding Nemo", imageName, description, year, length, rating, director, stars, url);

        year = "2005";
        length = "140 min";
        rating = 7.7;
        director = "George Lucas" ;
        stars = "Hayden Christensen, Natalie Portman, Ewan McGregor";
        imageName = "star_wars3.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "After three years of fighting in the Clone Wars, Anakin Skywalker falls prey to the Sith Lord's lies and makes an enemy of the Jedi and those he loves, concluding his journey to the Dark Side.";
        createAndAddMovie("Star Wars III", imageName, description, year, length, rating, director, stars, url);

        year = "2003";
        length = "201 min";
        rating = 8.9;
        director = "Peter Jackson" ;
        stars = "Elijah Wood, Viggo Mortensen, Ian McKellen";
        imageName = "rings2.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.";
        createAndAddMovie("The Lord of the Rings: The Return of the King", imageName, description, year, length, rating, director, stars, url);

        year = "2004";
        length = "127 min";
        rating = 7.4;
        director = "Sam Raimi" ;
        stars = "Tobey Maguire, Kirsten Dunst, Alfred Molina";
        imageName = "spiderman2.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Peter Parker is beset with troubles in his failing personal life as he battles a brilliant scientist named Doctor Otto Octavius.";
        createAndAddMovie("Spider-Man 2", imageName, description, year, length, rating, director, stars, url);

        year = "2013";
        length = "98 min";
        rating = 7.6;
        director = "Pierre Coffin, Chris Renaud" ;
        stars = "Steve Carell, Kristen Wiig, Benjamin Bratt";
        imageName = "despicable2.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Gru is recruited by the Anti-Villain League to help deal with a powerful new super criminal.";
        createAndAddMovie("Despicable Me 2", imageName, description, year, length, rating, director, stars, url);

        year = "2011";
        length = "154 min";
        rating = 6.4;
        director = "Michael Bay" ;
        stars = "Shia LaBeouf, Rosie Huntington-Whiteley, Tyrese Gibson";
        imageName = "transformers2.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "The Autobots learn of a Cybertronian spacecraft hidden on the moon, and race against the Decepticons to reach it and to learn its secrets.";
        createAndAddMovie("Transformers: Dark of the Moon", imageName, description, year, length, rating, director, stars, url);

        year = "2002";
        length = "179 min";
        rating = 8.8;
        director = "Peter Jackson" ;
        stars = "Elijah Wood, Ian McKellen, Viggo Mortensen";
        imageName = "rings.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "While Frodo and Sam edge closer to Mordor with the help of the shifty Gollum, the divided fellowship makes a stand against Sauron's new ally, Saruman, and his hordes of Isengard.";
        createAndAddMovie("The Lord of the Rings: The Two Towers", imageName, description, year, length, rating, director, stars, url);

        year = "2007";
        length = "139 min";
        rating = 6.3;
        director = "Sam Raimi" ;
        stars = "Tobey Maguire, Kirsten Dunst, Topher Grace";
        imageName = "spiderman3.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "A strange black entity from another world bonds with Peter Parker and causes inner turmoil as he contends with new villains, temptations, and revenge.";
        createAndAddMovie("Spider-Man 3", imageName, description, year, length, rating, director, stars, url);

        year = "2010";
        length = "108 min";
        rating = 6.5;
        director = "Tim Burton" ;
        imageName = "alice.jpg";
        url = imageStorageDomain+"/"+imageName;
        stars = "Mia Wasikowska, Johnny Depp, Helena Bonham Carter";
        description = "Nineteen-year-old Alice returns to the magical world from her childhood adventure, where she reunites with her old friends and learns of her true destiny: to end the Red Queen's reign of terror.";
        createAndAddMovie("Alice in Wonderland", imageName, description, year, length, rating, director, stars, url);

        year = "1994";
        length = "145 min";
        rating = 8.8;
        director = "Robert Zemeckis" ;
        stars = "Tom Hanks, Robin Wright, Gary Sinise";
        imageName = "forrest_gump.jpg";
        url = imageStorageDomain+"/"+imageName;
        description = "Forrest Gump, while not intelligent, has accidentally been present at many historic moments, but his true love, Jenny Curran, eludes him.";
        createAndAddMovie("Forrest Gump", imageName, description, year, length, rating, director, stars, url);
    }

    /*
    * Adds a Movie to the movie list
    */
    private void createAndAddMovie(String name, String imageName, String description, String year,
                                   String length, double rating, String director, String stars, String url) {
        Movie movieInfo = new Movie();
        movieInfo.imageName = imageName;
        movieInfo.name = name;
        movieInfo.description = description;
        movieInfo.year = year;
        movieInfo.length = length;
        movieInfo.rating = rating;
        movieInfo.director = director;
        movieInfo.stars = stars;
        movieInfo.url = url;

        this.moviesList.put(name, movieInfo);
    }
}
