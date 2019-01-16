package testing_ant;

public class SchemaDetail {
	private String schemaName;
	private String password;
	private String database;
	private String schemaDescription;
	private String projectName;
	private String dsName;
	private String schemaType;
	private String ipAddress;
	private String port;
	private String serviceName;
	private String tnsEntry;
	private String jndi;
	private String streamName;
	private String portability;
	
	

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}
	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	/**
	 * @return the schemaDescription
	 */
	public String getSchemaDescription() {
		return schemaDescription;
	}
	/**
	 * @param schemaDescription the schemaDescription to set
	 */
	public void setSchemaDescription(String schemaDescription) {
		this.schemaDescription = schemaDescription;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * @return the schemaDetailLabel
	 */
	public String getSchemaDetailLabel() {
		return (schemaName+"@" + database).toUpperCase();
	}
	/**
	 * @return the dsName
	 */
	public String getDsName() {
		return dsName;
	}
	/**
	 * @param dsName the dsName to set
	 */
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public String getSchemaType() {
		return schemaType;
	}
	public void setSchemaType(String schemaType) {
		this.schemaType = schemaType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTnsEntry() {
		return tnsEntry;
	}
	public void setTnsEntry(String tnsEntry) {
		this.tnsEntry = tnsEntry;
	}
	public String getJndi() {
		return jndi;
	}
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	public String getStreamName() {
		return streamName;
	}
	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	public String getPortability() {
		return portability;
	}
	public void setPortability(String portability) {
		this.portability = portability;
	}
}
