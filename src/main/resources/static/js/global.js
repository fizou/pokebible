/*

// Static Sample

var Pokemon = React.createClass({
  render: function() {
    return (
      <tr>
        <td>{this.props.pokemon.name}</td>
        <td>{this.props.pokemon.type}</td>
      </tr>);
  }
});

var PokemonTable = React.createClass({
  render: function() {
    var rows = [];
    this.props.pokemons.forEach(function(pokemon) {
      rows.push(<Pokemon pokemon={pokemon} />);
    });
    return (
<div className="container">
  <table className="table table-striped">
    <thead>
      <tr>
        <th>Name</th>
        <th>Type</th>
      </tr>
    </thead>
    <tbody>{rows}</tbody>
  </table>
</div>
	);
  }
});

var POKEMONS = [
  {name: 'Bulbizarre', type: 'Plante'},
  {name: 'Herbizarre', type: 'Plante'},
  {name: 'Florizarre', type: 'Plante'},
  {name: 'Salamèche', type: 'Feu'}
];

ReactDOM.render(
  <PokemonTable pokemons={POKEMONS} />, document.getElementById('root')
);

*/

// Dynamics Sample

var Pokemon = React.createClass({
  getInitialState: function() {
    return {display: true };
  },
  handleDelete() {
    var self = this;
    $.ajax({
      url: self.props.pokemon._links.self.href,
      type: 'DELETE',
      success: function(itemUrl) {
		toastr.success("Item deleted!");
        self.setState({display: false});
      },
      error: function(xhr, ajaxOptions, thrownError) {
        toastr.error(xhr.responseJSON.message);
      }
    });
  },
  render: function() {
    if (this.state.display==false) return null;
    else return (
      <tr>
		<td><img src={this.props.pokemon.picture} class="img-thumbnail" height='50px'/></td>
        <td>{this.props.pokemon.name}</td>
        <td>{this.props.pokemon.type}</td>
        <td>
          <button className="btn btn-info" onClick={this.handleDelete}>Delete</button>
        </td>
      </tr>
    );
  }
}); 

var PokemonTable = React.createClass({
  render: function() {
    var rows = [];
    this.props.pokemons.forEach(function(pokemon) {
      rows.push(<Pokemon pokemon={pokemon} />);
    });
    return (
<div className="container">
  <table className="table table-striped">
    <thead>
      <tr>
        <th>Picture</th>
        <th>Name</th>
        <th>Type</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>{rows}</tbody>
  </table>
</div>
	);
  }
});

var App = React.createClass({
 
  loadPokemonsFromServer: function () {
    var self = this;
    $.ajax({
      url: "api/pokemons"
    }).then(function (data) {
      self.setState({pokemons: data._embedded.pokemons});
    });
  },
 
  getInitialState: function () {
    return {pokemons: []};
  },
 
  componentDidMount: function () {
    this.loadPokemonsFromServer();
  },
 
  render() {
    return ( <PokemonTable pokemons={this.state.pokemons}/> );
  }
});

ReactDOM.render(<App />, document.getElementById('root') );
