DELETE FROM App WHERE Identifier LIKE 'Sample#%';

DELETE FROM ResourceGroup WHERE Identifier LIKE 'Sample#%';

DELETE FROM PrivacyLevel WHERE ResourceGroup_Identifier LIKE 'Sample#%';

DELETE FROM ServiceLevel WHERE App_Identifier LIKE 'Sample#%';

DELETE FROM ServiceLevel_PrivacyLevels WHERE App_Identifier LIKE 'Sample#%';