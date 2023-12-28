package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class AccountBase implements Account{
    protected AccountType type;
    protected String username;
    protected double balance;
    protected Course[] courses;
    protected int coursesSize;
    protected double[] grades;
    protected int gradesSize;
    {
        coursesSize=0;
        gradesSize=0;
    }
    public AccountBase(String username, double balance){
        this.username=username;
        this.balance=balance;
        courses=new Course[100];
        grades=new double[100];
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount){
        if(amount<0.0){
            throw new IllegalArgumentException();
        }
        balance+=amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if(course==null){
            throw new NullPointerException();
        }
        if(coursesSize==100){
            throw new MaxCourseCapacityReachedException("You have reached the max number of courser");
        }
        if(balance<course.getPrice())
            throw new InsufficientBalanceException("Not enough money");
        for (int i=0;i<courses.length;i++) {
            if (courses[i]!=null && courses[i].equals(course))
                throw new CourseAlreadyPurchasedException("This course is already purchased");
        }
        balance-=course.getPrice();
        courses[coursesSize++]=course;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if(course==null || resourcesToComplete==null){
            throw new IllegalArgumentException();
        }
        boolean isFound=false;
        for(int i=0;i<coursesSize;i++) {
            if(courses[i] != null && courses[i].equals(course)){
                isFound=true;
                for(int j=0;j< resourcesToComplete.length;j++){
                    courses[i].completeResource(resourcesToComplete[j]);
                }
            }
        }
        if(!isFound)
            throw new CourseNotPurchasedException("This course is not purchased");
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if(grade<2.00 || grade>6.00)
            throw new IllegalArgumentException();

        boolean isFound=false;
        for(int i=0;i<coursesSize;i++){
            if(courses[i] != null && courses[i].equals(course)){
                isFound=true;
                if(courses[i].isCompleted()) {
                    grades[gradesSize++] = grade;
                }
                else{
                    throw new CourseNotCompletedException("This course is not completed");
                }
            }
        }
        if(!isFound)
            throw new CourseNotPurchasedException("This course is not purchased");
    }

    @Override
    public Course getLeastCompletedCourse() {
        if(coursesSize==0)
            return null;
        Course min=courses[0];
        for(int i=1;i<coursesSize;i++){
            if(courses[i].getCompletionPercentage()<min.getCompletionPercentage()){
                min=courses[i];
            }
        }
        return min;
    }
}
