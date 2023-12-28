package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase{
    public EducationalAccount(String username, double balance){
        super(username,balance);
        type=(AccountType.EDUCATION);
    }
    private double getAverageScore(){
        double result=0.0;
        for(int i=0;i<gradesSize;i++){
            result+=grades[i];
        }
        return result/gradesSize;
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
        double discount=0.0;
        if(coursesSize%5==0 && getAverageScore()>=4.50) {
            discount = course.getPrice() * type.getDiscount();
        }
        balance-=course.getPrice()-discount;
        courses[coursesSize++]=course;
    }
}
