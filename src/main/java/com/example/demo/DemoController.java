package com.example.demo;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;
import org.noear.solon.annotation.Produces;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.smarthttp.integration.SmHttpPlugin;
import org.noear.solon.core.PluginEntity;
import org.noear.solon.core.util.MimeType;
import org.noear.solon.core.util.RunUtil;


@Controller
public class DemoController {
    @Produces(MimeType.TEXT_HTML_UTF8_VALUE)
    @Mapping("/")
    public String home() {
        HttpServerProps props = new HttpServerProps();
        String url = "/reboot?port=" + (props.getPort() + 1);
        return "你想改端口吗？点击：<a href='" + url + "'>" + url + "</a>";
    }

    @Produces(MimeType.TEXT_HTML_UTF8_VALUE)
    @Mapping("/hello")
    public String hello(@Param(defaultValue = "world") String name) {
        HttpServerProps props = new HttpServerProps();
        String url = "/reboot?port=" + (props.getPort() + 1);
        return String.format("Hello %s!", name) + " 你可能要改个端口？：<a href='" + url + "'>" + url + "</a>";
    }

    @Produces(MimeType.TEXT_HTML_UTF8_VALUE)
    @Mapping("/reboot")
    public String reboot(int port) throws Exception {
        if (port > 4000) {
            RunUtil.delay(() -> rebootDo(port), 50);//延后执行

            String url = "http://localhost:" + port + "/hello";
            return "开始切换端口...0.1秒后打开新地址：<a href='" + url + "'>" + url + "</a>";
        } else {
            return "端口太小";
        }
    }

    private void rebootDo(int port) {
        PluginEntity pluginEntity = Solon.app().pluginGet(SmHttpPlugin.class);
        //停止
        pluginEntity.stop();

        //启动
        Solon.cfg().setProperty("server.http.port", String.valueOf(port));
        pluginEntity.start(Solon.context());
    }
}