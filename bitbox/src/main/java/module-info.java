/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module bitbox {
  requires java.base;
  requires java.compiler;
  requires tinylog.api;
  requires tinylog.impl;
  requires transitive toolbox.tools;
  exports us.pserver.bitbox;
  exports us.pserver.bitbox.impl;
  exports us.pserver.bitbox.transform;
}
