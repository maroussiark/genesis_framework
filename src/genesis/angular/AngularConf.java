package genesis.angular;

import genesis.Constantes;
import genesis.Entity;
import handyman.HandyManUtils;

public class AngularConf {

    private String getTemplContent(String path) throws Exception {
        String content = HandyManUtils
                .getFileContent(Constantes.DATA_PATH + "/" + path + "." + Constantes.MODEL_TEMPLATE_EXT);
        return content;
    }

    // ----------------
    // App-module
    // ----------------

    // Importation service

    private String[] getServiceImport(Entity[] entities) throws Exception {

        String[] result = new String[2];

        String importTempl = this.getTemplContent("angular/app/app-module/service_import");

        String importRep = "";
        String providers = "";
        boolean first = true;
        for (Entity entity : entities) {
            if (!first) {
                providers = "," + providers;
            }
            String temp = importTempl;
            temp = temp.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
            temp = temp.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
            importRep += temp;
            providers += entity.getClassName() + "Service";
        }

        result[0] = importRep;
        result[1] = providers;
        return result;
    }

    // Creating App - module

    private void generateAppModule(Entity[] entities, String projectName) throws Exception {
        String module = this.getTemplContent("angular/app/app-module/app.module");
        String[] service_import = this.getServiceImport(entities);

        module = module.replace("[service_import]", service_import[0]);
        module = module.replace("[providers_service]", service_import[1]);

        String path = "angular/" + HandyManUtils.majStart(projectName) + "/src/app/app.module.ts";

        HandyManUtils.createFile(path);
        HandyManUtils.overwriteFileContent(path, module);
    }

    // ----------------
    // App-components.ts
    // ----------------

    // Get menu
    private String getAppPages(Entity[] entities) throws Exception {
        String result = "";

        String importTempl = this.getTemplContent("angular/app/app-component/app_pages");

        boolean first = true;
        for (Entity entity : entities) {
            if (!first) {
                result = "," + result;
            }
            String temp = importTempl;
            temp = temp.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
            temp = temp.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));
            result += temp;
        }

        return result;
    }

    // Creating App-component
    private void generateAppComponent(Entity[] entities, String projectName) throws Exception {
        String module = this.getTemplContent("angular/app/app-component/app-component");
        String menu = this.getAppPages(entities);

        module = module.replace("[menu]", menu);

        String path = "angular/" + HandyManUtils.majStart(projectName) + "/src/app/app.component.ts";

        HandyManUtils.createFile(path);
        HandyManUtils.overwriteFileContent(path, module);
    }

    // ----------------
    // App-routing-module.ts
    // ----------------

    // All link in the route
    private String getLink(Entity[] entities) throws Exception {

        String routePathTempl = this.getTemplContent("angular/app/app-routing/link");

        String rep = "";
        boolean isFirst = true;
        for (Entity entity : entities) {
            String temp = routePathTempl;
            if (!isFirst) {
                temp = "," + temp;
            }
            temp = temp.replace("[classNameMaj]", HandyManUtils.majStart(entity.getClassName()));
            temp = temp.replace("[classNameMin]", HandyManUtils.minStart(entity.getClassName()));

            rep += temp;
            isFirst = false;
        }

        return rep;
    }

    // Generating App
    private void generateAppRoutingModule(Entity[] entities, String projectName) throws Exception {

        String routing = this.getTemplContent("angular/app/app-routing/app-routing.module");

        routing = routing.replace("[link]", this.getLink(entities));

        String path = "angular/" + HandyManUtils.majStart(projectName) + "/src/app/app-routing.module.ts";

        HandyManUtils.createFile(path);
        HandyManUtils.overwriteFileContent(path, routing);
    }

    // GENERATE GENERAL APP
    public void generateAllApp(Entity[] entities, String projectName) throws Exception {
        this.generateAppComponent(entities, projectName);
        this.generateAppRoutingModule(entities, projectName);
        this.generateAppModule(entities, projectName);
    }

}
