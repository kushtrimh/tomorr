package com.kushtrimh.tomorr.thymeleaf;

import com.kushtrimh.tomorr.configuration.TestThymeleafConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("thymeleaf"), @Tag("integration")})
@ContextConfiguration(classes = {TestThymeleafConfiguration.class})
@ExtendWith({SpringExtension.class})
public class ThymeleafResolverTest {

    @Autowired
    private ITemplateEngine iTemplateEngine;

    @Test
    public void resolve_WhenCalledWithValidTemplateName_ReturnsTemplateContent() {
        Context context = new Context();
        String templateContent = iTemplateEngine.process("new-release-notification", context);
        Assertions.assertThat(templateContent).contains("Notification from <span class=\"font-bold\">tomorr</span>");
    }
}
