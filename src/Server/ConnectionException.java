package Server;
class ConnectionException extends Exception { 
  Exception e;
  ConnectionException(Exception e) { this.e = e; }
  ConnectionException(String msg) { super(msg); e=this; }
}