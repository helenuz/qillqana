package pe.edu.unsaac.in.qillqana.common.command;

public class QueryCommand extends Command{
	
	public static final String LIST_STUDENT_LESSONS="list_student_lessons";
	public static final String LIST_TEACHER_LESSON="list_teacher_lessons";
	
	public QueryCommand() {
		super();
		setName("query");
	}
	
	public void setType(String type) {
		addParameter("type", type);
	}
	
	public void setQueryName(String name){
		addParameter("query_name", name);
	}
	
	public void setUserId(String id){
		addParameter("teacher_id", id);
	}
}
