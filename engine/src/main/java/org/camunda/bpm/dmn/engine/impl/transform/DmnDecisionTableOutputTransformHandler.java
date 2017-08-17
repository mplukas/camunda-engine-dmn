/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.dmn.engine.impl.transform;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.dmn.engine.impl.DmnDecisionTableOutputImpl;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnElementTransformContext;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnElementTransformHandler;
import org.camunda.bpm.dmn.engine.impl.spi.type.DmnDataTypeTransformer;
import org.camunda.bpm.dmn.engine.impl.spi.type.DmnTypeDefinition;
import org.camunda.bpm.dmn.engine.impl.type.DefaultTypeDefinition;
import org.camunda.bpm.dmn.engine.impl.type.DmnTypeDefinitionImpl;
import org.camunda.bpm.model.dmn.instance.Output;
import org.camunda.bpm.model.dmn.instance.OutputValues;

public class DmnDecisionTableOutputTransformHandler implements DmnElementTransformHandler<Output, DmnDecisionTableOutputImpl> {

  @Override
  public DmnDecisionTableOutputImpl handleElement(DmnElementTransformContext context, Output output) {
    return createFromOutput(context, output);
  }

  protected DmnDecisionTableOutputImpl createFromOutput(DmnElementTransformContext context, Output output) {
    DmnDecisionTableOutputImpl decisionTableOutput = createDmnElement(context, output);

    decisionTableOutput.setId(output.getId());
    decisionTableOutput.setName(output.getLabel());
    decisionTableOutput.setOutputName(output.getName());
    decisionTableOutput.setTypeDefinition(getTypeDefinition(context, output));
    decisionTableOutput.setOutputValues(getOutputValues(output.getOutputValues()));
    
    return decisionTableOutput;
  }

  private List<String> getOutputValues(OutputValues outputValues) {
    // assume that the output values are values separated by ','
    final String values = outputValues.getText().getTextContent();
    String[] arr = values.split(",");
    List<String> valueList = new ArrayList<String>();
    for (String v : arr)
    {
      valueList.add(v.trim());
    }
    return valueList;
  }

  protected DmnDecisionTableOutputImpl createDmnElement(DmnElementTransformContext context, Output output) {
    return new DmnDecisionTableOutputImpl();
  }

  protected DmnTypeDefinition getTypeDefinition(DmnElementTransformContext context, Output output) {
    String typeRef = output.getTypeRef();
    if (typeRef != null) {
      DmnDataTypeTransformer transformer = context.getDataTypeTransformerRegistry().getTransformer(typeRef);
      return new DmnTypeDefinitionImpl(typeRef, transformer);
    }
    else {
      return new DefaultTypeDefinition();
    }
  }

}
