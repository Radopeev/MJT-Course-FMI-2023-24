package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

import java.util.Objects;

public class Course implements Completable,Purchasable{
    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private Category category;
    private boolean isPurchased;

    public Course(String name, String description, double price, Resource[] content, Category category)
    {
        this.name=name;
        this.description=description;
        this.price=price;
        this.content=content;
        this.category=category;
    }
    /**
     * Returns the name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {
        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {
        return CourseDuration.of(content);
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws IllegalArgumentException if resourceToComplete is null.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if(resourceToComplete==null){
            throw new IllegalArgumentException();
        }
        boolean isFound=false;
        for(int i=0;i<content.length;i++){
            if(content[i]!=null && Objects.equals(content[i],resourceToComplete)){
                content[i].complete();
                isFound=true;
                break;
            }
        }
        if(!isFound)
            throw new ResourceNotFoundException("Resource not found");
    }
    @Override
    public boolean isCompleted() {
        for(int i=0;i<content.length;i++){
            if(!content[i].isCompleted())
                return false;
        }
        return true;
    }

    @Override
    public int getCompletionPercentage() {
        int sum=0;
        for(int i=0;i<content.length;i++){
            sum+=content[i].getCompletionPercentage();
        }
        return (int) Math.ceil((double) sum /content.length);
    }

    @Override
    public void purchase() {
        isPurchased=true;
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }
}
