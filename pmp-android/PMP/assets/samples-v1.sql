INSERT INTO "App" VALUES('Sample#pmp.test.app1','Test App 1','This is a sample App for testing database configuration', 0);
INSERT INTO "App" VALUES('Sample#pmp.test.app2','Test App 2','This is another sample App for testing database configuration', 0);

INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg1','read','Read Files','Enable this to allow reading files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg1','write','Write Files','Enable this to allow writing files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg1','delete','Delete Files','Enable this to allow deleting files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg1','create','Create Files','Enable this to allow creating files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg2','select','SELECT Statement','Enable this to allow using the SELECT Statement');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg2','insert','INSERT Statement','Enable this to allow using the INSERT Statement');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg2','delete','DELETE Statement','Enable this to allow using the DELETE Statement');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg2','truncate','TRUNCATE Statement','Enable this to allow using the TRUNCATE Statement');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg3','minimumSpeed','Minimum Speed','The minimum speed which is required as a maxima return');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.rg3','inaccurateFactor','Inaccurate Factor','The factor of inaccuration for the actual position');

INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.rg1','File System','Just an example');
INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.rg2','Database','Just another example');
INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.rg3','Location','Just a last example');

INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app1',0,'Initial Service Level','No functionallity at all');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app2',0,'Initial Service Level','No functionallity at all');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app1',1,'Full Service Level','Many functions are enabled by this Service Level');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app2',1,'First Service Level','Some functions are enabled');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app2',2,'Second Service Level','Some more functions are enabled');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.app2',3,'Last Service Level','All functions are enabled');

INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app1',1,'Sample#pmp.test.rg1','read','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app1',1,'Sample#pmp.test.rg1','write','true');

INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',1,'Sample#pmp.test.rg2','select','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',2,'Sample#pmp.test.rg2','select','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',3,'Sample#pmp.test.rg2','select','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',2,'Sample#pmp.test.rg2','insert','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',3,'Sample#pmp.test.rg2','insert','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',3,'Sample#pmp.test.rg2','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.app2',3,'Sample#pmp.test.rg3','minimumSpeed','100');
