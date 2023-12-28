package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform{
    private Account[] accounts;
    private Course[] courses;
    public Udemy(Account[] accounts, Course[] courses){
        this.accounts=accounts;
        this.courses=courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if(name==null)
            throw new IllegalArgumentException();

        for(int i=0;i<courses.length;i++){
            if(courses[i]!=null && courses[i].getName().equals(name)){
                return courses[i];
            }
        }
        throw new CourseNotFoundException("The course was not found");
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if(keyword==null ||keyword.isBlank())
            throw new IllegalArgumentException();
        int count=0;
        for(int i=0;i<courses.length;i++){
            if(courses[i]!=null && courses[i].getName().contains(keyword) ||
                    courses[i].getDescription().contains(keyword)){
                count++;
            }
        }
        Course[] result=new Course[count];
        int size=0;
        for(int i=0;i<courses.length;i++){
            if(courses[i]!=null && courses[i].getName().contains(keyword) ||
                    courses[i].getDescription().contains(keyword)){
                result[size++]=courses[i];
            }
        }
        return result;
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if(category==null){
            throw new IllegalArgumentException();
        }
        int count=0;
        for(int i=0;i<courses.length;i++){
            if(courses[i]!=null && courses[i].getCategory()==category){
                count++;
            }
        }
        Course[] result=new Course[count];
        int size=0;
        for(int i=0;i<courses.length;i++){
            if(courses[i]!=null && courses[i].getCategory()==category){
                result[size++]=courses[i];
            }
        }
        return result;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        for(int i=0;i<accounts.length;i++) {
            if (accounts[i]!=null && accounts[i].getUsername().equals(name)) {
                return accounts[i];
            }
        }
        throw new AccountNotFoundException("This account was not found.");
    }

    @Override
    public Course getLongestCourse() {
        if(courses.length==0){
            return null;
        }
        Course temp=courses[0];
        for(int i=1;i<courses.length;i++){
            if(courses[i].getTotalTime().hours()>temp.getTotalTime().hours()){
                temp=courses[i];
            }else if (courses[i].getTotalTime().minutes()>temp.getTotalTime().minutes()){
                temp=courses[i];
            }
        }
        return temp;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if(courses.length==0){
            return null;
        }
        if(category==null) {
            throw new IllegalArgumentException();
        }
        int i=0;
        Course start = null;
        for(;i<courses.length;i++){
            if(courses[i].getCategory()==category){
                start=courses[i];
                break;
            }
        }
        for(;i<courses.length;i++){
            if(courses[i].getPrice()<start.getPrice()){
                start=courses[i];
            }
        }
        return start;
    }
}
