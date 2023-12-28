package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {
    public CourseDuration{
        if(hours<0 || hours>24 || minutes<0 || minutes>60){
            throw new IllegalArgumentException();
        }
    }
    public static CourseDuration of(Resource[] content){
       int sum=0;
       for(int i=0;i< content.length;i++){
           sum+=content[i].getDuration().minutes();
       }
       int hours=sum/60,minutes=sum%60;
       return new CourseDuration(hours,minutes);
   }
}
