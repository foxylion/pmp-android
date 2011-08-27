/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: B:\\Repositories\\pmp-android\\pmp-android\\PMP-API\\src\\de\\unistuttgart\\ipvs\\pmp\\service\\IResourceGroupService.aidl
 */
package de.unistuttgart.ipvs.pmp.service;
public interface IResourceGroupService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements de.unistuttgart.ipvs.pmp.service.IResourceGroupService
{
private static final java.lang.String DESCRIPTOR = "de.unistuttgart.ipvs.pmp.service.IResourceGroupService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an de.unistuttgart.ipvs.pmp.service.IResourceGroupService interface,
 * generating a proxy if needed.
 */
public static de.unistuttgart.ipvs.pmp.service.IResourceGroupService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof de.unistuttgart.ipvs.pmp.service.IResourceGroupService))) {
return ((de.unistuttgart.ipvs.pmp.service.IResourceGroupService)iin);
}
return new de.unistuttgart.ipvs.pmp.service.IResourceGroupService.Stub.Proxy(obj);
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
case TRANSACTION_getPrivacyLevelIdentifiers:
{
data.enforceInterface(DESCRIPTOR);
java.util.List _result = this.getPrivacyLevelIdentifiers();
reply.writeNoException();
reply.writeList(_result);
return true;
}
case TRANSACTION_getPrivacyLevelName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.getPrivacyLevelName(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getPrivacyLevelDescription:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.getPrivacyLevelDescription(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getHumanReadablePrivacyLevelValue:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _result = this.getHumanReadablePrivacyLevelValue(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setAccesses:
{
data.enforceInterface(DESCRIPTOR);
java.util.Map _arg0;
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_arg0 = data.readHashMap(cl);
this.setAccesses(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_satisfiesPrivacyLevel:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
boolean _result = this.satisfiesPrivacyLevel(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements de.unistuttgart.ipvs.pmp.service.IResourceGroupService
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
/**
	 * @returns the name of the ResourceGroup.
	 */
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
/**
	 * @returns the description of the ResourceGroup.
	 */
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
/**
	 * Returns the privacy levels for the ResourceGroup.
	 */
public java.util.List getPrivacyLevelIdentifiers() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPrivacyLevelIdentifiers, _data, _reply, 0);
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
public java.lang.String getPrivacyLevelName(java.lang.String locale, java.lang.String identifier) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
_data.writeString(identifier);
mRemote.transact(Stub.TRANSACTION_getPrivacyLevelName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getPrivacyLevelDescription(java.lang.String locale, java.lang.String identifier) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
_data.writeString(identifier);
mRemote.transact(Stub.TRANSACTION_getPrivacyLevelDescription, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getHumanReadablePrivacyLevelValue(java.lang.String locale, java.lang.String identifier, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(locale);
_data.writeString(identifier);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_getHumanReadablePrivacyLevelValue, _data, _reply, 0);
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
	 * Accepts a Map with key as String and value as String.
	 * The value has to be a List with concatenations (using ":" as delimiter)
	 * of privacy-level and value of the given privacy level inside.<br/>
	 * <br/>
	 * Exact declaration: Map&lt;String, List&lt;String&gt;&gt;
	 */
public void setAccesses(java.util.Map accesses) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeMap(accesses);
mRemote.transact(Stub.TRANSACTION_setAccesses, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
	 * Checks if an privacy level value satisfies the currently set value or not.
	 * 
	 * @param reference the original value which should be referenced for testing
	 * @param value the value which should be compared to reference
	 *
	 * @return true if value is equal or better than reference, otherwise false
	 */
public boolean satisfiesPrivacyLevel(java.lang.String privacyLevel, java.lang.String reference, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(privacyLevel);
_data.writeString(reference);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_satisfiesPrivacyLevel, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getDescription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPrivacyLevelIdentifiers = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getPrivacyLevelName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getPrivacyLevelDescription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getHumanReadablePrivacyLevelValue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_setAccesses = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_satisfiesPrivacyLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
/**
	 * @returns the name of the ResourceGroup.
	 */
public java.lang.String getName(java.lang.String locale) throws android.os.RemoteException;
/**
	 * @returns the description of the ResourceGroup.
	 */
public java.lang.String getDescription(java.lang.String locale) throws android.os.RemoteException;
/**
	 * Returns the privacy levels for the ResourceGroup.
	 */
public java.util.List getPrivacyLevelIdentifiers() throws android.os.RemoteException;
public java.lang.String getPrivacyLevelName(java.lang.String locale, java.lang.String identifier) throws android.os.RemoteException;
public java.lang.String getPrivacyLevelDescription(java.lang.String locale, java.lang.String identifier) throws android.os.RemoteException;
public java.lang.String getHumanReadablePrivacyLevelValue(java.lang.String locale, java.lang.String identifier, java.lang.String value) throws android.os.RemoteException;
/**
	 * Accepts a Map with key as String and value as String.
	 * The value has to be a List with concatenations (using ":" as delimiter)
	 * of privacy-level and value of the given privacy level inside.<br/>
	 * <br/>
	 * Exact declaration: Map&lt;String, List&lt;String&gt;&gt;
	 */
public void setAccesses(java.util.Map accesses) throws android.os.RemoteException;
/**
	 * Checks if an privacy level value satisfies the currently set value or not.
	 * 
	 * @param reference the original value which should be referenced for testing
	 * @param value the value which should be compared to reference
	 *
	 * @return true if value is equal or better than reference, otherwise false
	 */
public boolean satisfiesPrivacyLevel(java.lang.String privacyLevel, java.lang.String reference, java.lang.String value) throws android.os.RemoteException;
}
