import java.util.*;

public class Movies{ 
  private String dirn; 
  private String ftitle; 
  private String fyear;
  private ArrayList<String> moviesList;
   
  public Movies(String dirn, String ftitle, String fyear, ArrayList<String> moviesList ) { 
    this.dirn = dirn; 
    this.ftitle = ftitle; 
    this.fyear = fyear;
    this.moviesList = new ArrayList<String>();
    for(int i=0;i<moviesList.size();i++ ){
       this.moviesList.add(moviesList.get(i));
    }
  }
  
  public void setdirn(String cdirn){
      this.dirn = cdirn;
  }
  public void setftitle(String cftitle){
      this.ftitle = cftitle;
  }
  public void setfyear(String cfyear){
      this.fyear = cfyear;
  }
  public String getdirn(){
      return this.dirn;
  }
  public String getftitle(){
      return this.ftitle;
  }
  public String getfyear(){
      return this.fyear;
  }
  public ArrayList<String> getmoviesList(){
      return this.moviesList;
  }
}