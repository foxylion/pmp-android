/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: B:\\Repositories\\pmp-android\\pmp-android\\PMP-API\\src\\de\\unistuttgart\\ipvs\\pmp\\service\\IAppService.aidl
 */
package de.unistuttgart.ipvs.pmp.service;
public interface IAppService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements de.unistuttgart.ipvs.pmp.service.IAppService
{
private static final java.lang.String DESCRIPTOR = "de.unistuttgart.ipvs.pmp.service.IAppService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an de.unistuttgart.ipvs.pmp.service.IAppService interface,
 * generating a proxy if needed.
 */
public static de.unistuttgart.ipvs.pmp.service.IAppService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof de.unistuttgart.ipvs.pmp.service.IAppService))) {
return ((de.unistuttgart.ipvs.pmp.service.IAppService)iin);
}
return new de.unistuttgart.ipvs.pmp.service.IAppService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getName(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getDescription:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getDescription(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getServiceLevelCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getServiceLevelCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getServiceLevelName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _result = this.getServiceLevelName(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getServiceLevelDescription:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _result = this.getServiceLevelDescription(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getServiceLevelPrivacyLevels:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.util.List _result = this.getServiceLevelPrivacyLevels(_arg0);
reply.writeNoException();
reply.writeList(_result);
return true;
}
case TRANSACTION_setServiceLevel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setServiceLevel(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements de.unistuttgart.ipvs.pmp.service.IAppService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public java.lang.String getName(java.lang.String locale) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getDescription(java.lang.String locale) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
mRemote.transact(Stub.TRANSACTION_getDescription, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getServiceLevelCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServiceLevelCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getServiceLevelName(java.lang.String locale, int serviceLevelId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
_data.writeInt(serviceLevelId);
mRemote.transact(Stub.TRANSACTION_getServiceLevelName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getServiceLevelDescription(java.lang.String locale, int serviceLevelId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
_data.writeInt(serviceLevelId);
mRemote.transact(Stub.TRANSACTION_getServiceLevelDescription, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * @return a List of Strings, which concatenates
	 * 	       ResourceGroup, PrivacyLevel and Value,
	 *         using a ":" as delemiter.
	 */
public java.util.List getServiceLevelPrivacyLevels(int serviceLevelId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(serviceLevelId);
mRemote.transact(Stub.TRANSACTION_getServiceLevelPrivacyLevels, _data, _reply, 0);
_reply.readException();
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_result = _reply.readArrayList(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setServiceLevel(int serviceLevel) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(serviceLevel);
mRemote.transact(Stub.TRANSACTION_setServiceLevel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getDescription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getServiceLevelCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getServiceLevelName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getServiceLevelDescription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getServiceLevelPrivacyLevels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setServiceLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
public java.lang.String getName(java.lang.String locale) throws android.os.RemoteException;
public java.lang.String getDescription(java.lang.String locale) throws android.os.RemoteException;
public int getServiceLevelCount() throws android.os.RemoteException;
public java.lang.String getServiceLevelName(java.lang.String locale, int serviceLevelId) throws android.os.RemoteException;
public java.lang.String getServiceLevelDescription(java.lang.String locale, int serviceLevelId) throws android.os.RemoteException;
/**
	 * @return a List of Strings, which concatenates
	 * 	       ResourceGroup, PrivacyLevel and Value,
	 *         using a ":" as delemiter.
	 */
public java.util.List getServiceLevelPrivacyLevels(int serviceLevelId) throws android.os.RemoteException;
public void setServiceLevel(int serviceLevel) throws android.os.RemoteException;
}
