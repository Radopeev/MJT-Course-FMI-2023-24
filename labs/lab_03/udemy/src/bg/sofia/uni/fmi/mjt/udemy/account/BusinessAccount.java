package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase{
    private Category[] allowedCategories;
    public BusinessAccount(String username, double balance, Category[] allowedCategories){
        super(username,balance);
        this.allowedCategories=allowedCategories;
        type=AccountType.BUSINESS;
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
        boolean isFound=false;
        for(int i=0;i<allowedCategories.length;i++){
            if(allowedCategories[i]!=null && allowedCategories[i].equals(course.getCategory())){
                isFound=true;
            }
        }
        if(!isFound){
            throw new IllegalArgumentException();
        }
        for (int i=0;i<courses.length;i++) {
            if (courses[i] != null && courses[i].equals(course))
                throw new CourseAlreadyPurchasedException("This course is already purchased");
        }
        double discount=course.getPrice()* type.getDiscount();
        balance-=course.getPrice()-discount;
        courses[coursesSize++]=course;
    }
}
