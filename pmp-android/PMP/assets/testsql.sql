INSERT INTO "App" VALUES('Sample#pmp.test.okapp','OK App','This is conformaly defined App', 0);
INSERT INTO "App" VALUES('Sample#pmp.test.noname','','This app have no name', 0);
INSERT INTO "App" VALUES(,'TestNoIdentifier','This app have no Identifier', 0);
INSERT INTO "App" VALUES('','TestEmptyIdentifier','This app have no Identifier', 0);
INSERT INTO "App" VALUES('Sample#pmp.test.nodescription','TestNoDescription','', 0);
INSERT INTO "App" VALUES('Sample#pmp.test.nolevel','TestNoLevel','This app has no level',);
INSERT INTO "App" VALUES('Sample#pmp.test.falselevel','TestFalseLevel','This app has false level set',5);
INSERT INTO "App" VALUES('Sample#pmp.test.okapp','OK App2nd','This is conformaly defined App 2nd', 0);

INSERT INTO "ResourceGroup" VALUES('','TestResEmptyIdentifier','This ressource has empty identifier');
INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.resnoname','','This ressource has no name');
INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.resnodescription','TestResNoDescription','');
INSERT INTO "ResourceGroup" VALUES(,'TestResNoIdentifier','This ressource has no identifier');
INSERT INTO "ResourceGroup" VALUES('Sample#pmp.test.resnoname','SameIdentifier','This ressource has same identifier resnoname');


INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.okapp',0,'ZeroLevel','ZeroLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.okapp',1,'FirstLevel','FirstLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.okapp',2,'SecondLevel','SecondLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.noname',0,'ZeroLevel','ZeroLevel');
INSERT INTO "ServiceLevel" VALUES('',0,'ZeroLevel','ZeroLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.nodescription',0,'ZeroLevel','ZeroLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.falselevel',3,'ThirdLevel','ThirdLevel');
INSERT INTO "ServiceLevel" VALUES('Sample#pmp.test.falselevel',6,'SixthLevel','SixthLevel');

INSERT INTO "PrivacyLevel" VALUES('','read','Read Files','Enable this to allow reading files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.resnoname','write','Write Files','Enable this to allow writing files');
INSERT INTO "PrivacyLevel" VALUES('Sample#pmp.test.resnodescription','delete','Delete Files','Enable this to allow deleting files');

INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.okapp',0,'Sample#pmp.test.resnoname','write','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.okapp',1,'Sample#pmp.test.resnodescription','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.okapp',2,'Sample#pmp.test.resnoname','write','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.okapp',2,'Sample#pmp.test.resnodescription','delete','true');

INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.noname',0,'Sample#pmp.test.resnodescription','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.noname',0,'Sample#pmp.test.resNoRes','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('',0,'Sample#pmp.test.resnodescription','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.falselevel',3,'Sample#pmp.test.resnodescription','delete','true');
INSERT INTO "ServiceLevel_PrivacyLevels" VALUES('Sample#pmp.test.falselevel',6,'Sample#pmp.test.resnodescription','delete','true');
