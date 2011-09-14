CREATE TABLE IF NOT EXISTS ResourceGroup (
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(Identifier)
);

CREATE TABLE IF NOT EXISTS PrivacyLevel (
	ResourceGroup_Identifier TEXT NOT NULL,
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(ResourceGroup_Identifier,
	            Identifier)
);

CREATE TABLE IF NOT EXISTS ResourceContext (
	ResourceGroup_Identifier TEXT NOT NULL,
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(ResourceGroup_Identifier,
	            Identifier)
);

CREATE TABLE IF NOT EXISTS Preset (
	Name TEXT NOT NULL,
	Description TEXT,
	Type TEXT,
	Identifier TEXT,
	PRIMARY KEY(Name, Type, Identifier)
);

CREATE TABLE IF NOT EXISTS Preset_PrivacyLevels (
	Preset_Name TEXT NOT NULL,
	Preset_Type TEXT,
	Preset_Identifier TEXT,
	ResourceGroup_Identifier TEXT NOT NULL,
	PrivacyLevel_Identifier TEXT NOT NULL,
	Value TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            Preset_Type,
				Preset_Identifier,
				ResourceGroup_Identifier,
				PrivacyLevel_Identifier)
);

CREATE TABLE IF NOT EXISTS Preset_Apps (
	Preset_Name TEXT NOT NULL,
	Preset_Type TEXT,
	Preset_Identifier TEXT,
	App_Identifier TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            Preset_Type,
				Preset_Identifier,
	            App_Identifier)
);

CREATE TABLE IF NOT EXISTS Context_Preset (
	Preset_Name TEXT NOT NULL,
	Preset_Type TEXT,
	Preset_Identifier TEXT,
	ResourceGroup_Identifier TEXT NOT NULL,
	ResourceContext_Identifier TEXT NOT NULL,
	Value TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
	            Preset_Type,
				Preset_Identifier,
	            ResourceGroup_Identifier,
				ResourceContext_Identifier)
);

CREATE TABLE IF NOT EXISTS Context_Preset_PrivacyLevels (
	Preset_Name TEXT NOT NULL,
	Preset_Type TEXT,
	Preset_Identifier TEXT,
	PrivacyLevel_ResourceGroup_Identifier TEXT NOT NULL,
	PrivacyLevel_Identifier TEXT NOT NULL,
	ResourceContext_ResourceGroup_Identifier TEXT NOT NULL,
	ResourceContext_Identifier TEXT NOT NULL,
	PRIMARY KEY(Preset_Name,
				Preset_Type,
				Preset_Identifier,
	            PrivacyLevel_ResourceGroup_Identifier,
				PrivacyLevel_Identifier,
				ResourceContext_ResourceGroup_Identifier,
				ResourceContext_Identifier)
);

CREATE TABLE IF NOT EXISTS App (
	Identifier TEXT NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	ServiceLevel_Active INTEGER NOT NULL,
	PRIMARY KEY(Identifier)
);

CREATE TABLE IF NOT EXISTS ServiceLevel (
	App_Identifier TEXT NOT NULL,
	Level INTEGER NOT NULL,
	Name_Cache TEXT,
	Description_Cache TEXT,
	PRIMARY KEY(App_Identifier,
	            Level)
);

CREATE TABLE IF NOT EXISTS ServiceLevel_PrivacyLevels (
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