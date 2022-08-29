class Solution {
    public boolean containsDuplicate(int[] nums){
        //to sort the array of integers 
        Arrays.sort(nums);
        //to itterate through the array of integers checking for duplicates
        for(int i = 0; i < nums.length - 1; i++){
            //checks element adjacent for possible equality.
            if(nums[i] == nums[i + 1]) return true;
            
        }
        return false;
    }
}