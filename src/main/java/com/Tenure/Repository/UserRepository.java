package com.Tenure.Repository;



public interface UserRepository {
	
}

/*public interface UserRepository extends CrudRepository<UserMaster, Long> {

	@Query("SELECT usermst FROM User_Master usermst  WHERE usermst.contactNumber=?1 and usermst.loginPassword=?2")
	UserMaster getUserDetailsByMobileNumber(String contactNumber, String loginPassword);


	@SuppressWarnings("unchecked")
	@Transactional
	public UserMaster save(UserMaster usermaster);

}*/
