# JDBC experiments
This project is for my education in JDBC drivers field. It's helps me to dig deeper into the driver internals.

## Postgres 

### connection to db process from scratch:
1) o.postgresql.Driver - loading driver configuration via classloader, Connecting with URL: jdbc:postgresql
2)  o.p.j.PgConnection - configuring the connection options (fetch size, threshold)
3) ConnectionFactoryImpl - setting the protocol
4) Encoding - preparing the UTF-8 options by default (or another encoding)
5) ConnectionFactoryImpl - set buffer size (buffer for what? WAL?), SSL settings config via o.p.j.s.ScramAuthenticator

###  query preparation:
1) o.p.c.v.QueryExecutorImpl - preparing parameters using ParameterStatus( timezone, transactions, encoding), ReadyForQuery(I) 
2) QueryExecutorImpl (Parse params, Bind params, complete query execution preparing)
3) PgConnection - datatypes for binary send, binary receive
4) QueryExecutorImpl - handler=org.postgresql.jdbc.PgStatement, execute

### insert query preparation:
1)  query preparation (org.postgresql.jdbc.PgPreparedStatement)
2) org.postgresql.jdbc.PgConnection$TransactionCommandHandler
3) QueryExecutorImpl -  FE=> Parse(stmt=S_1,query="COMMIT",oids={})
4) autocommit options for the PgConnection


### select query preparation:
1)  query preparation (org.postgresql.jdbc.PgStatement)
2) QueryExecutorImpl -  <=BE RowDescription (Field(id,UUID,16,T) , Field(properties,JSON,65535,T) )
3) mapping datarows
4) o.p.j.PgConnection -   getString columnIndex: 1

### callable query preparation:
1)  query preparation (org.postgresql.jdbc.PgCallableStatement)
2) org.postgresql.jdbc.CallableBatchResultHandler
3) parsing results

## Ms SQL

### connection to db process from scratch:
1) c.m.s.jdbc.Driver - reading properties for further connection. 
2) SQLServerDriver - Property:serverName Value:localhost , Property:databaseName Value:msdb
3) c.m.s.j.i.SQLServerConnection - ConnectionID:1 Connecting with server: localhost port: 1433
4) ConnectionID:1 ClientConnectionId: 0657650e-4050-446b-9308-dc6224f1abb0 - further work with c.m.s.j.i.TDS.DATA
5) c.m.s.j.i.T.Channel - com.microsoft.sqlserver.jdbc.TDSChannel$ProxyInputStream@7d900ecf Reading 4096 bytes
6) c.m.s.j.i.T.Channel - TDSChannel (ConnectionID:1) Getting TLS or better SSL context

###  query preparation:
1) interation between the c.m.s.j.i.T.Command, c.m.s.j.i.TDS.Reader and c.m.s.j.Connection
2) SQLServerStatement properties prepare Result type:1003 (2003) Concurrency:1007 Fetchsize:128
3) preparing query (adding arguments, parsing query)
4) execution c.m.s.j.i.SQLServerStatement - SQLServerStatement:1 Executing (not server cursor) 
5) c.m.s.j.i.TDS.DATA - /127.0.0.1:49978 SPID:52 TDSWriter@43df23d3 (ConnectionID:1) sending packet (258 bytes) - sending package in bynary format

### insert query preparation:
1) query preparation
2) c.m.s.j.Connection - ENTRY INSERT INTO ACCOUNTS (ID, NAME, PROPERTIES, PHOTO) VALUES (?, ?, ?, ?) 
3) c.m.s.j.i.SQLServerStatement - SQLServerPreparedStatement:2
4) TDSCommand starting the request to the database
5) set autocommit to true (set i.m.p.l.ic.i.t._.t.r.a.ns.a.c.t.i.o.n.s..o.n.
6) interation between the c.m.s.j.i.T.Command and c.m.s.j.i.TDS.Writer for sending the TDS.DATA with insert command


### select query preparation:
1) query preparation
2) SQLServerStatement: interation between the c.m.s.j.i.T.Command and c.m.s.j.i.TDS.Writer for sending the TDS.DATA with select command
3) c.m.s.j.i.TDS.TOKEN - TDSReader ClientCursorInitializer: Processing TDS_COLMETADATA -> getNextResult
4) c.m.s.jdbc.Statement - RETURN SQLServerResultSet
4) c.m.s.jdbc.ResultSet - RETURN 8FA5030E-EE5B-4D93-A786-B5DDC9CAE468