package techsayas.in.psctrolls.psctroll;

/**
 * Created by vamsi on 18-Jul-16.
 */

public class Otherptofilevisit {

    public String photo1;
    public String moviePoster;
    public float movieRating;
    public String id;

    public Otherptofilevisit(){

    }
    public Otherptofilevisit(String photo1, String moviePoster, float movieRating,String id){
        this.photo1 = photo1;
        this.moviePoster = moviePoster;
        this.movieRating  = movieRating;
        this.id = id;

    }

    public String getPhoto1() {
        return photo1;
    }
    public String getId() {
        return id;
    }

    public void setMovieName(String movieName) {
        this.photo1 = movieName;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(float movieRating) {
        this.movieRating = movieRating;
    }
}
