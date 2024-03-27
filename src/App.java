import genesis.Constantes;
import genesis.Credentials;
import genesis.CustomChanges;
import genesis.CustomFile;
import genesis.Database;
import genesis.Entity;
import genesis.EntityField;
import genesis.Language;
import genesis.angular.AngularConf;
import handyman.HandyManUtils;
import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

public class App {

        public static void main(String[] args) throws Exception {
                Database[] databases = HandyManUtils.fromJson(
                                Database[].class,
                                HandyManUtils.getFileContent(Constantes.DATABASE_JSON));
                Language[] languages = HandyManUtils.fromJson(
                                Language[].class,
                                HandyManUtils.getFileContent(Constantes.LANGUAGE_JSON));
                Database database;
                Language language;
                String databaseName, user, pwd, host;
                boolean useSSL, allowPublicKeyRetrieval;
                String projectName, entityName;
                Credentials credentials;
                String projectNameTagPath, projectNameTagContent;
                File project, credentialFile;
                String customFilePath, customFileContentOuter;
                Entity[] entities;
                String[] models, controllers, views, services, servicespecs, componentServices, viewFiles,
                                componentServiceFiles, specs,
                                specFiles, modules, moduleFiles, routings, routingFiles;
                String modelFile, controllerFile, customFile, serviceFile, servicespecFile;
                String customFileContent;
                String foreignContext;
                String customChanges, changesFile;
                String navLink, navLinkPath;
                String[] suffixes_pages = {
                                "-list",
                                "-update",
                                "-form",
                                "-delete"
                };
                try {
                        try (Scanner scanner = new Scanner(System.in)) {
                                System.out.println("Choose a database engine:");
                                for (int i = 0; i < databases.length; i++) {
                                        System.out.println((i + 1) + ") " + databases[i].getNom());
                                }
                                System.out.print("> ");
                                database = databases[scanner.nextInt() - 1];
                                System.out.println("Choose a framework:");
                                for (int i = 0; i < languages.length; i++) {
                                        System.out.println((i + 1) + ") " + languages[i].getNom());
                                }
                                System.out.print("> ");
                                language = languages[scanner.nextInt() - 1];
                                System.out.println(language.getView_directory());
                                System.out.println("Enter your database credentials:");
                                System.out.print("Database name: ");
                                databaseName = scanner.next();
                                System.out.print("Username: ");
                                user = scanner.next();
                                System.out.print("Password: ");
                                pwd = scanner.next();
                                System.out.print("Database host: ");
                                host = scanner.next();
                                System.out.print("Use SSL ?(Y/n): ");
                                useSSL = scanner.next().equalsIgnoreCase("Y");
                                System.out.print("Allow public key retrieval ?(Y/n): ");
                                allowPublicKeyRetrieval = scanner.next().equalsIgnoreCase("Y");
                                System.out.println();
                                System.out.print("Enter your project name: ");
                                projectName = scanner.next();
                                System.out.print("Which entities to import ?(* to select all): ");
                                entityName = scanner.next();
                                credentials = new Credentials(
                                                databaseName,
                                                user,
                                                pwd,
                                                host,
                                                useSSL,
                                                allowPublicKeyRetrieval);
                                project = new File(projectName);
                                project.mkdir();

                                File angularProject = new File("angular/" + HandyManUtils.majStart(projectName));
                                angularProject.mkdirs();

                                for (CustomFile c : language.getAdditionnalFiles()) {
                                        customFilePath = c.getName();
                                        customFilePath = customFilePath.replace(
                                                        "[projectNameMaj]",
                                                        HandyManUtils.majStart(projectName));
                                        HandyManUtils.createFile(customFilePath);
                                        customFileContentOuter = HandyManUtils
                                                        .getFileContent(Constantes.DATA_PATH + "/" + c.getContent())
                                                        .replace("[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName));
                                        HandyManUtils.overwriteFileContent(
                                                        customFilePath,
                                                        customFileContentOuter);
                                }
                                HandyManUtils.extractDir(
                                                Constantes.DATA_PATH + "/" + language.getSkeleton() + "."
                                                                + Constantes.SKELETON_EXTENSION,
                                                project.getPath());

                                HandyManUtils.extractDir(Constantes.DATA_PATH + "/" +
                                                language.getView_directory(),
                                                angularProject.getPath());

                                credentialFile = new File(project.getPath(), Constantes.CREDENTIAL_FILE);
                                credentialFile.createNewFile();
                                HandyManUtils.overwriteFileContent(
                                                credentialFile.getPath(),
                                                HandyManUtils.toJson(credentials));
                                for (String replace : language.getProjectNameTags()) {
                                        projectNameTagPath = replace
                                                        .replace("[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName))
                                                        .replace("[projectNameMin]",
                                                                        HandyManUtils.minStart(projectName));
                                        projectNameTagContent = HandyManUtils
                                                        .getFileContent(projectNameTagPath)
                                                        .replace("[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName));
                                        projectNameTagContent = projectNameTagContent.replace(
                                                        "[databaseHost]",
                                                        credentials.getHost());
                                        projectNameTagContent = projectNameTagContent.replace("[databasePort]",
                                                        database.getPort());
                                        projectNameTagContent = projectNameTagContent.replace(
                                                        "[databaseName]",
                                                        credentials.getDatabaseName());
                                        projectNameTagContent = projectNameTagContent.replace("[user]",
                                                        credentials.getUser());
                                        projectNameTagContent = projectNameTagContent.replace("[pwd]",
                                                        credentials.getPwd());
                                        projectNameTagContent = projectNameTagContent.replace(
                                                        "[projectNameMin]",
                                                        HandyManUtils.minStart(projectName));
                                        HandyManUtils.overwriteFileContent(
                                                        projectNameTagPath,
                                                        projectNameTagContent);
                                }
                                try (Connection connect = database.getConnexion(credentials)) {
                                        entities = database.getEntities(connect, credentials, entityName);
                                        for (int i = 0; i < entities.length; i++) {
                                                entities[i].initialize(connect, credentials, database, language);
                                        }
                                        models = new String[entities.length];
                                        controllers = new String[entities.length];
                                        views = new String[entities.length * 3];
                                        componentServices = new String[entities.length * 3];
                                        specs = new String[entities.length * 3];
                                        modules = new String[entities.length * 3];
                                        routings = new String[entities.length * 3];
                                        services = new String[entities.length];
                                        servicespecs = new String[entities.length];
                                        navLink = "";
                                        for (int i = 0; i < models.length; i++) {
                                                models[i] = language.generateModel(entities[i], projectName);
                                                controllers[i] = language.generateController(
                                                                entities[i],
                                                                database,
                                                                credentials,
                                                                projectName);
                                                services[i] = language.generateService(entities[i]);
                                                servicespecs[i] = language.generateServicespec(entities[i]);
                                                views[i * 3] = language.generateView(entities[i], projectName,
                                                                language.getView().getViewContent().get("liste"));
                                                views[(i * 3) + 1] = language.generateView(entities[i], projectName,
                                                                language.getView().getViewContent().get("modifier"));
                                                views[(i * 3) + 2] = language.generateView(entities[i], projectName,
                                                                language.getView().getViewContent().get("form"));

                                                componentServices[i * 3] = language.generateComponentService(
                                                                entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getComponentServiceContent()
                                                                                .get("liste"));
                                                componentServices[(i * 3) + 1] = language.generateComponentService(
                                                                entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getComponentServiceContent()
                                                                                .get("modifier"));
                                                componentServices[(i * 3) + 2] = language.generateComponentService(
                                                                entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getComponentServiceContent()
                                                                                .get("form"));

                                                specs[i * 3] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getSpecContent().get("liste"));
                                                specs[(i * 3) + 1] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getSpecContent().get("modifier"));
                                                specs[(i * 3) + 2] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getSpecContent().get("form"));

                                                modules[i * 3] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getModuleContent().get("liste"));
                                                modules[(i * 3) + 1] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getModuleContent().get("modifier"));
                                                modules[(i * 3) + 2] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getModuleContent().get("form"));

                                                routings[i * 3] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getRoutingModuleContent()
                                                                                .get("liste"));
                                                routings[(i * 3) + 1] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getRoutingModuleContent()
                                                                                .get("modifier"));
                                                routings[(i * 3) + 2] = language.generateComponentService(entities[i],
                                                                projectName, language.getComponentService()
                                                                                .getRoutingModuleContent().get("form"));

                                                modelFile = language
                                                                .getModel()
                                                                .getModelSavePath()
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                modelFile = modelFile.replace(
                                                                "[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                System.out.println(modelFile);
                                                controllerFile = language
                                                                .getController()
                                                                .getControllerSavepath()
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                serviceFile = language.getService().getServiceSavePath().replace(
                                                                "[projectName]",
                                                                projectName);
                                                servicespecFile = language.getService().getServiceSavePath().replace(
                                                                "[projectName]",
                                                                projectName);
                                                controllerFile = controllerFile.replace(
                                                                "[projectNameMin]",
                                                                HandyManUtils.minStart(projectName));
                                                modelFile += "/" +
                                                                HandyManUtils.majStart(entities[i].getClassName()) +
                                                                "." +
                                                                language.getModel().getModelExtension();
                                                controllerFile += "/" +
                                                                HandyManUtils.majStart(entities[i].getClassName()) +
                                                                language.getController().getControllerNameSuffix() +
                                                                "." +
                                                                language.getController().getControllerExtension();
                                                serviceFile += "/" + language.getService().getServiceName() + "."
                                                                + language.getService().getServiceExtension();
                                                serviceFile = serviceFile.replace("[classNameMin]",
                                                                HandyManUtils.minStart(entities[i].getClassName()));
                                                servicespecFile += "/" + language.getService().getSpecName() + "."
                                                                + language.getService().getServiceExtension();
                                                servicespecFile = servicespecFile.replace("[classNameMin]",
                                                                HandyManUtils.minStart(entities[i].getClassName()));
                                                viewFiles = new String[3];
                                                componentServiceFiles = new String[3];
                                                specFiles = new String[3];
                                                moduleFiles = new String[3];
                                                routingFiles = new String[3];
                                                viewFiles[0] = language.getView().getViewSavePath().get("liste")
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                viewFiles[1] = language.getView().getViewSavePath().get("modifier")
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                viewFiles[2] = language.getView().getViewSavePath().get("form").replace(
                                                                "[projectNameMaj]",
                                                                HandyManUtils.majStart(projectName));

                                                viewFiles[0] += "/" + language.getView().getViewName().get("liste")
                                                                + "."
                                                                + language.getView().getViewExtension();
                                                viewFiles[1] += "/" + language.getView().getViewName().get("modifier")
                                                                + "."
                                                                + language.getView().getViewExtension();
                                                viewFiles[2] += "/" + language.getView().getViewName().get("form") + "."
                                                                + language.getView().getViewExtension();

                                                componentServiceFiles[0] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("liste")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                componentServiceFiles[1] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("modifier")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                componentServiceFiles[2] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("form")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));

                                                componentServiceFiles[0] += "/"
                                                                + language.getComponentService()
                                                                                .getComponentServiceName()
                                                                                .get("liste")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                componentServiceFiles[1] += "/"
                                                                + language.getComponentService()
                                                                                .getComponentServiceName()
                                                                                .get("modifier")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                componentServiceFiles[2] += "/"
                                                                + language.getComponentService()
                                                                                .getComponentServiceName()
                                                                                .get("form")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();

                                                specFiles[0] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("liste")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                specFiles[1] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("modifier")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                specFiles[2] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("form")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                specFiles[0] += "/"
                                                                + language.getComponentService().getSpecName()
                                                                                .get("liste")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                specFiles[1] += "/"
                                                                + language.getComponentService().getSpecName()
                                                                                .get("modifier")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                specFiles[2] += "/"
                                                                + language.getComponentService().getSpecName()
                                                                                .get("form")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();

                                                moduleFiles[0] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("liste")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                moduleFiles[1] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("modifier")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                moduleFiles[2] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("form")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));

                                                moduleFiles[0] += "/"
                                                                + language.getComponentService().getModuleName()
                                                                                .get("liste")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                moduleFiles[1] += "/"
                                                                + language.getComponentService().getModuleName()
                                                                                .get("modifier")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                moduleFiles[2] += "/"
                                                                + language.getComponentService().getModuleName()
                                                                                .get("form")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();

                                                routingFiles[0] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("liste")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                routingFiles[1] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("modifier")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                routingFiles[2] = language.getComponentService()
                                                                .getComponentServiceSavePath().get("form")
                                                                .replace("[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));

                                                routingFiles[0] += "/"
                                                                + language.getComponentService().getRoutingModuleName()
                                                                                .get("liste")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                routingFiles[1] += "/"
                                                                + language.getComponentService().getRoutingModuleName()
                                                                                .get("modifier")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();
                                                routingFiles[2] += "/"
                                                                + language.getComponentService().getRoutingModuleName()
                                                                                .get("form")
                                                                + "."
                                                                + language.getComponentService().getFileExtension();

                                                for (int j = 0; j < viewFiles.length; j++) {
                                                        viewFiles[j] = viewFiles[j].replace("[classNameMin]",
                                                                        HandyManUtils.minStart(
                                                                                        entities[i].getClassName()));
                                                        componentServiceFiles[j] = componentServiceFiles[j].replace(
                                                                        "[classNameMin]",
                                                                        HandyManUtils.minStart(
                                                                                        entities[i].getClassName()));
                                                        specFiles[j] = specFiles[j].replace(
                                                                        "[classNameMin]",
                                                                        HandyManUtils.minStart(
                                                                                        entities[i].getClassName()));
                                                        moduleFiles[j] = moduleFiles[j].replace(
                                                                        "[classNameMin]",
                                                                        HandyManUtils.minStart(
                                                                                        entities[i].getClassName()));
                                                        routingFiles[j] = routingFiles[j].replace(
                                                                        "[classNameMin]",
                                                                        HandyManUtils.minStart(
                                                                                        entities[i].getClassName()));

                                                }
                                                HandyManUtils.createFile(modelFile);
                                                new AngularConf().generateAllApp(entities, projectName);

                                                for (CustomFile f : language
                                                                .getModel()
                                                                .getModelAdditionnalFiles()) {
                                                        foreignContext = "";
                                                        for (EntityField ef : entities[i].getFields()) {
                                                                if (ef.isForeign()) {
                                                                        foreignContext += language.getModel()
                                                                                        .getModelForeignContextAttr();
                                                                        foreignContext = foreignContext.replace(
                                                                                        "[classNameMaj]",
                                                                                        HandyManUtils.majStart(
                                                                                                        ef.getType()));
                                                                }
                                                        }
                                                        customFile = f
                                                                        .getName()
                                                                        .replace(
                                                                                        "[classNameMaj]",
                                                                                        HandyManUtils.majStart(
                                                                                                        entities[i].getClassName()));
                                                        customFile = language
                                                                        .getModel()
                                                                        .getModelSavePath()
                                                                        .replace(
                                                                                        "[projectNameMaj]",
                                                                                        HandyManUtils.majStart(
                                                                                                        projectName))
                                                                        +
                                                                        "/" +
                                                                        customFile;
                                                        customFile = customFile.replace(
                                                                        "[projectNameMin]",
                                                                        HandyManUtils.minStart(projectName));
                                                        customFileContent = HandyManUtils
                                                                        .getFileContent(Constantes.DATA_PATH + "/"
                                                                                        + f.getContent())
                                                                        .replace(
                                                                                        "[classNameMaj]",
                                                                                        HandyManUtils.majStart(
                                                                                                        entities[i].getClassName()));
                                                        customFileContent = customFileContent.replace(
                                                                        "[projectNameMin]",
                                                                        HandyManUtils.minStart(projectName));
                                                        customFileContent = customFileContent.replace(
                                                                        "[projectNameMaj]",
                                                                        HandyManUtils.majStart(projectName));
                                                        customFileContent = customFileContent.replace(
                                                                        "[databaseHost]",
                                                                        credentials.getHost());
                                                        customFileContent = customFileContent.replace(
                                                                        "[databaseName]",
                                                                        credentials.getDatabaseName());
                                                        customFileContent = customFileContent.replace("[user]",
                                                                        credentials.getUser());
                                                        customFileContent = customFileContent.replace("[pwd]",
                                                                        credentials.getPwd());
                                                        customFileContent = customFileContent.replace(
                                                                        "[modelForeignContextAttr]",
                                                                        foreignContext);
                                                        HandyManUtils.createFile(customFile);
                                                        HandyManUtils.overwriteFileContent(customFile,
                                                                        customFileContent);
                                                }
                                                HandyManUtils.createFile(controllerFile);
                                                // HandyManUtils.createFile(viewFile);
                                                HandyManUtils.createFile(serviceFile);
                                                HandyManUtils.createFile(servicespecFile);
                                                HandyManUtils.overwriteFileContent(serviceFile, services[i]);
                                                HandyManUtils.overwriteFileContent(servicespecFile, servicespecs[i]);
                                                HandyManUtils.overwriteFileContent(modelFile, models[i]);
                                                HandyManUtils.overwriteFileContent(controllerFile, controllers[i]);
                                                for (int j = 0; j < viewFiles.length; j++) {
                                                        HandyManUtils.createFile(viewFiles[j]);
                                                        HandyManUtils.createFile(componentServiceFiles[j]);
                                                        HandyManUtils.createFile(specFiles[j]);
                                                        HandyManUtils.createFile(moduleFiles[j]);
                                                        HandyManUtils.createFile(routingFiles[j]);

                                                        HandyManUtils.overwriteFileContent(viewFiles[j],
                                                                        views[(i * 3) + j]);
                                                        HandyManUtils.overwriteFileContent(componentServiceFiles[j],
                                                                        componentServices[(i * 3) + j]);
                                                        HandyManUtils.overwriteFileContent(specFiles[j],
                                                                        specs[(i * 3) + j]);
                                                        HandyManUtils.overwriteFileContent(moduleFiles[j],
                                                                        modules[(i * 3) + j]);
                                                        HandyManUtils.overwriteFileContent(routingFiles[j],
                                                                        routings[(i * 3) + j]);
                                                }
                                                // HandyManUtils.overwriteFileContent(viewFile, views[i]);
                                                // navLink+=language.getNavbarLinks().getLink();
                                                // navLink=navLink.replace("[projectNameMaj]",
                                                // HandyManUtils.majStart(projectName));
                                                // navLink=navLink.replace("[projectNameMin]",
                                                // HandyManUtils.minStart(projectName));
                                                // navLink=navLink.replace("[classNameMin]",
                                                // HandyManUtils.minStart(entities[i].getClassName()));
                                                // navLink=navLink.replace("[classNameMaj]",
                                                // HandyManUtils.majStart(entities[i].getClassName()));
                                                // navLink=navLink.replace("[classNameformattedMin]",
                                                // HandyManUtils.minStart(HandyManUtils.formatReadable(entities[i].getClassName())));
                                                // navLink=navLink.replace("[classNameformattedMaj]",
                                                // HandyManUtils.majStart(HandyManUtils.formatReadable(entities[i].getClassName())));
                                        }
                                        // navLinkPath=language.getNavbarLinks().getPath().replace("[projectNameMaj]",
                                        // HandyManUtils.majStart(projectName));
                                        // navLinkPath=navLinkPath.replace("[projectNameMin]",
                                        // HandyManUtils.minStart(projectName));
                                        // System.out.println(navLinkPath);
                                        // HandyManUtils.overwriteFileContent(navLinkPath,
                                        // HandyManUtils.getFileContent(navLinkPath).replace("[navbarLinks]", navLink));
                                        for (CustomChanges c : language.getCustomChanges()) {
                                                customChanges = "";
                                                for (Entity e : entities) {
                                                        customChanges += c.getChanges();
                                                        customChanges = customChanges.replace(
                                                                        "[classNameMaj]",
                                                                        HandyManUtils.majStart(e.getClassName()));
                                                        customChanges = customChanges.replace(
                                                                        "[classNameMin]",
                                                                        HandyManUtils.minStart(e.getClassName()));
                                                        customChanges = customChanges.replace("[databaseHost]",
                                                                        credentials.getHost());
                                                        customChanges = customChanges.replace("[user]",
                                                                        credentials.getUser());
                                                        customChanges = customChanges.replace(
                                                                        "[databaseName]",
                                                                        credentials.getDatabaseName());
                                                        customChanges = customChanges.replace("[pwd]",
                                                                        credentials.getPwd());
                                                }
                                                if (c.isWithEndComma() == false) {
                                                        customChanges = customChanges.substring(0,
                                                                        customChanges.length() - 1);
                                                }
                                                changesFile = c
                                                                .getPath()
                                                                .replace(
                                                                                "[projectNameMaj]",
                                                                                HandyManUtils.majStart(projectName));
                                                HandyManUtils.overwriteFileContent(
                                                                changesFile,
                                                                HandyManUtils
                                                                                .getFileContent(changesFile)
                                                                                .replace("[customChanges]",
                                                                                                customChanges));
                                        }
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
