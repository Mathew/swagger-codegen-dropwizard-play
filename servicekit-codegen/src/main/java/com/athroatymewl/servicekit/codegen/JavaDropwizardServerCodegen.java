package com.athroatymewl.servicekit.codegen;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.AbstractJavaCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaDropwizardServerCodegen extends AbstractJavaCodegen implements CodegenConfig{

	protected String implFolder = "src/main/java";
	private static final String DEFAULT_LIBRARY = "jersey2";
	private static final String DROPWIZARD_TEMPLATE_DIRECTORY_NAME = "JavaDropwizard";

	public JavaDropwizardServerCodegen() {
		super();

		outputFolder = "generated-code/JavaJaxRS-Jersey";
		this.cliOptions.add(new CliOption("implFolder", "folder for generated implementation code"));

		apiTestTemplateFiles.clear();

		this.importMapping.remove("ApiModelProperty");
		this.importMapping.remove("ApiModel");

		// clear model and api doc template as this codegen
		// does not support auto-generated markdown doc at the moment
		modelDocTemplateFiles.remove("model_doc.mustache");
		apiDocTemplateFiles.remove("api_doc.mustache");

		writeOptional(outputFolder, new SupportingFile("pom.mustache", "", "pom.xml"));

		embeddedTemplateDir = templateDir = DROPWIZARD_TEMPLATE_DIRECTORY_NAME;

		CliOption library = new CliOption(CodegenConstants.LIBRARY, "library template (sub-template) to use");

		library.setDefault(DEFAULT_LIBRARY);
	}

	@Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName()
    {
        return "dropwizard";
    }

    @Override
    public String getHelp()
    {
        return "Generates a JavaDropwizard Server application.";
    }

	@Override
	public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
		String basePath = resourcePath;
		if (basePath.startsWith("/")) {
			basePath = basePath.substring(1);
		}
		int pos = basePath.indexOf("/");
		if (pos > 0) {
			basePath = basePath.substring(0, pos);
		}

		if (basePath == "") {
			basePath = "default";
		} else {
			if (co.path.startsWith("/" + basePath)) {
				co.path = co.path.substring(("/" + basePath).length());
			}
			co.subresourceOperation = !co.path.isEmpty();
		}
		List<CodegenOperation> opList = operations.get(basePath);
		if (opList == null) {
			opList = new ArrayList<CodegenOperation>();
			operations.put(basePath, opList);
		}
		opList.add(co);
		co.baseName = basePath;
	}

	@Override
	public CodegenModel fromModel(String name, Model model, Map<String, Model> allDefinitions) {
		CodegenModel codegenModel = super.fromModel(name, model, allDefinitions);
		if(codegenModel.description != null) {
			codegenModel.imports.remove("ApiModel");
		}

		return codegenModel;
	}



	@Override
	public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
		super.postProcessModelProperty(model, property);

		if(!BooleanUtils.toBoolean(Boolean.valueOf(model.isEnum))) {
			model.imports.remove("ApiModelProperty");
			model.imports.remove("ApiModel");
		}

		if("null".equals(property.example)) {
			property.example = null;
		}

		//Add imports for Jackson
		if(!BooleanUtils.toBoolean(model.isEnum)) {
			model.imports.add("JsonProperty");

			if(BooleanUtils.toBoolean(model.hasEnums)) {
				model.imports.add("JsonValue");
			}
		}
	}

	@Override
	public void processOpts() {
		super.processOpts();

		if(this.additionalProperties.containsKey("implFolder")) {
			this.implFolder = (String)this.additionalProperties.get("implFolder");
		}

		if ( additionalProperties.containsKey(CodegenConstants.IMPL_FOLDER)) {
			this.implFolder = (String) additionalProperties.get(CodegenConstants.IMPL_FOLDER);
		}

		/*
		if ("joda".equals(dateLibrary)) {
			supportingFiles.add(new SupportingFile("JodaDateTimeProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JodaDateTimeProvider.java"));
			supportingFiles.add(new SupportingFile("JodaLocalDateProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JodaLocalDateProvider.java"));
		} else if ( dateLibrary.startsWith("java8") ) {
			supportingFiles.add(new SupportingFile("OffsetDateTimeProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "OffsetDateTimeProvider.java"));
			supportingFiles.add(new SupportingFile("LocalDateProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "LocalDateProvider.java"));
		}
		*/

		writeOptional(outputFolder, new SupportingFile("pom.mustache", "", "pom.xml"));
		/*
		writeOptional(outputFolder, new SupportingFile("README.mustache", "", "README.md"));
		supportingFiles.add(new SupportingFile("ApiException.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiException.java"));
		supportingFiles.add(new SupportingFile("ApiOriginFilter.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiOriginFilter.java"));
		supportingFiles.add(new SupportingFile("ApiResponseMessage.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "ApiResponseMessage.java"));
		supportingFiles.add(new SupportingFile("NotFoundException.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "NotFoundException.java"));
		supportingFiles.add(new SupportingFile("jacksonJsonProvider.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "JacksonJsonProvider.java"));
		supportingFiles.add(new SupportingFile("RFC3339DateFormat.mustache", (sourceFolder + '/' + apiPackage).replace(".", "/"), "RFC3339DateFormat.java"));
		*/
	}
}
