CREATE TABLE ResourceGroup (
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(Identifier)
);

CREATE TABLE PrivacyLevel (
	ResourceGroup_Identifier TEXT NOT NULL,
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(ResourceGroup_Identifier,
	            Identifier)
);

CREATE TABLE ResourceContext (
	ResourceGroup_Identifier TEXT NOT NULL,
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(ResourceGroup_Identifier,
	            Identifier)
);

CREATE TABLE Preset (
	Name TEXT NOT NULL,
	ResourceGroup_Identifier TEXT,
	Description TEXT,
	PRIMARY KEY(Name, ResourceGroup_Identifier)
);

CREATE TABLE Preset_PrivacyLevels (
	Preset_Name TEXT NOT NULL,
	ResourceGroup_Identifier TEXT NOT NULL,
	ResourceContext_Identifier TEXT NOT NULL,
	Value TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            ResourceGroup_Identifier,
				ResourceContext_Identifier)
);

CREATE TABLE Preset_Apps (
	Preset_Name TEXT NOT NULL,
	App_Identifier TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            App_Identifier)
);

CREATE TABLE Context_Preset (
	Preset_Name TEXT NOT NULL,
	ResourceGroup_Identifier TEXT NOT NULL,
	ResourceContext_Identifier TEXT NOT NULL,
	Value TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            ResourceGroup_Identifier,
				ResourceContext_Identifier)
);

CREATE TABLE Context_Preset_PrivacyLevels (
	Preset_Name TEXT NOT NULL,
	PrivacyLevel_ResourceGroup_Identifier TEXT NOT NULL,
	PrivacyLevel_Identifier TEXT NOT NULL,
	ResourceContext_ResourceGroup_Identifier TEXT NOT NULL,
	ResourceContext_Identifier TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            PrivacyLevel_ResourceGroup_Identifier,
				PrivacyLevel_Identifier,
				ResourceContext_ResourceGroup_Identifier,
				ResourceContext_Identifier)
);

CREATE TABLE App (
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(Identifier)
);

CREATE TABLE ServiceLevel (
	App_Identifier TEXT NOT NULL,
	Level INTEGER NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(App_Identifier,
	            Level)
);

CREATE TABLE ServiceLevel_PrivacyLevels (
	App_Identifier TEXT NOT NULL,
	ServiceLevel_Level INTEGER NOT NULL,
	ResourceGroup_Identifier TEXT NOT NULL,
	PrivacyLevel_Identifier TEXT NOT NULL,
	Value TEXT NOT NULL,
	PRIMARY KEY(App_Identifier,
	            ServiceLevel_Level,
				ResourceGroup_Identifier,
				PrivacyLevel_Identifier)
);