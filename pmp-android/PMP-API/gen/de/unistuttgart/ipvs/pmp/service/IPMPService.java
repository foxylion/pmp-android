/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: B:\\Repositories\\pmp-android\\pmp-android\\PMP-API\\src\\de\\unistuttgart\\ipvs\\pmp\\service\\IPMPService.aidl
 */
package de.unistuttgart.ipvs.pmp.service;
public interface IPMPService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements de.unistuttgart.ipvs.pmp.service.IPMPService
{
private static final java.lang.String DESCRIPTOR = "de.unistuttgart.ipvs.pmp.service.IPMPService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an de.unistuttgart.ipvs.pmp.service.IPMPService interface,
 * generating a proxy if needed.
 */
public static de.unistuttgart.ipvs.pmp.service.IPMPService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof de.unistuttgart.ipvs.pmp.service.IPMPService))) {
return ((de.unistuttgart.ipvs.pmp.service.IPMPService)iin);
}
return new de.unistuttgart.ipvs.pmp.service.IPMPService.Stub.Proxy(obj);
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
case TRANSACTION_registerApp:
{
data.enforceInterface(DESCRIPTOR);
de.unistuttgart.ipvs.pmp.app.App _arg0;
if ((0!=data.readInt())) {
_arg0 = de.unistuttgart.ipvs.pmp.app.App.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
boolean _result = this.registerApp(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_registerResourceGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.registerResourceGroup(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_savePrivacyLevel:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.savePrivacyLevel(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements de.unistuttgart.ipvs.pmp.service.IPMPService
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
public boolean registerApp(de.unistuttgart.ipvs.pmp.app.App app) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((app!=null)) {
_data.writeInt(1);
app.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_registerApp, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean registerResourceGroup(java.lang.String resourceGroup) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(resourceGroup);
mRemote.transact(Stub.TRANSACTION_registerResourceGroup, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void savePrivacyLevel(java.lang.String app, java.lang.String resourceGroup, java.lang.String privacyLevel, java.lang.String value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(app);
_data.writeString(resourceGroup);
_data.writeString(privacyLevel);
_data.writeString(value);
mRemote.transact(Stub.TRANSACTION_savePrivacyLevel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerResourceGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_savePrivacyLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public boolean registerApp(de.unistuttgart.ipvs.pmp.app.App app) throws android.os.RemoteException;
public boolean registerResourceGroup(java.lang.String resourceGroup) throws android.os.RemoteException;
public void savePrivacyLevel(java.lang.String app, java.lang.String resourceGroup, java.lang.String privacyLevel, java.lang.String value) throws android.os.RemoteException;
}
